package org.techuni.TechUniInviteSystem.service;

import discord4j.discordjson.json.AllowedMentionsData;
import discord4j.discordjson.json.MemberData;
import discord4j.discordjson.json.MessageCreateRequest;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.techuni.TechUniInviteSystem.config.DiscordConfig;
import org.techuni.TechUniInviteSystem.controller.response.invite.DiscordJoinSuccessResponse;
import org.techuni.TechUniInviteSystem.domain.invite.InviteDto;
import org.techuni.TechUniInviteSystem.domain.invite.models.DiscordInviteModel;
import org.techuni.TechUniInviteSystem.error.ErrorCode;
import org.techuni.TechUniInviteSystem.error.MyHttpException;
import org.techuni.TechUniInviteSystem.external.discord.DiscordAPI;
import org.techuni.TechUniInviteSystem.external.discord.template.DiscordTemplateEngine;
import org.techuni.TechUniInviteSystem.external.discord.template.IDiscordMessageVariables;
import org.techuni.TechUniInviteSystem.external.discord.template.variables.JoinServerDMVariable;
import org.techuni.TechUniInviteSystem.service.DiscordDMService.DiscordMessageAttachment;
import org.techuni.TechUniInviteSystem.service.invite.DiscordInviteService;

@Service
@Slf4j
@AllArgsConstructor
public class DiscordAPIService {

    private final DiscordConfig config;
    private final InviteService inviteService;
    private final DiscordInviteService discordInviteService;
    private final DiscordTemplateEngine templateEngine;
    private final DiscordDMService discordDMService;

    public DiscordJoinSuccessResponse joinGuild(final DiscordAPI api, final InviteDto inviteDto) {
        final var invite = inviteDto.intoModel(DiscordInviteModel.class);
        final var discordInvite = invite.getAdditionalData();

        final var guildIdStr = discordInvite.getGuildID();
        final var guildId = Long.parseLong(guildIdStr);

        if (api.isGuildMember(guildId)) {
            throw ErrorCode.DISCORD_INVITATION_ALREADY_JOINED.exception(String.valueOf(invite.getDbId()), invite.getInvitationCode().toString(),
                    guildIdStr, api.userString());
        }

        // useの設定を優先 (useされていない -> 無限使用を防止する)
        inviteService.useInvite(inviteDto);
        final MemberData memberData;
        try {
            memberData = api.joinGuild(guildId, discordInvite.getNickname());
        } catch (MyHttpException e) {
            // ALREADY_JOINEDのparamセット & useステータスのリセット処理(ALREADY_JOINEDのときは再使用可能に)
            if (e.getErrorCode().equals(ErrorCode.DISCORD_INVITATION_ALREADY_JOINED)) {
                inviteService.revertUseInvite(inviteDto);
                throw ErrorCode.DISCORD_INVITATION_ALREADY_JOINED.exception(String.valueOf(invite.getDbId()), invite.getInvitationCode().toString(),
                        guildIdStr, api.userString());
            } else {
                throw e;
            }
        }
        final var userData = memberData.user();
        final var userId = userData.id().asLong();

        discordInviteService.setJoinedUser(invite.getDbId(), userId);

        /* DM送信 */
        final var dmVariable = new JoinServerDMVariable( //
                userId, //
                Optional.ofNullable(invite.getAdditionalData().getNickname()).orElse(userData.username()) //
        );
        try {
            scheduleJoinGuildDM(userId, dmVariable);
        } catch (Exception e) {
            log.error("Some error occurred while scheduling DM to user. (JoinServerDM)", e);
        }

        return new DiscordJoinSuccessResponse(guildIdStr);
    }

    public void scheduleJoinGuildDM(long userId, IDiscordMessageVariables variables) {
        final var attachments = config.getJoinServerDMAttachment() //
                .map(resource -> new DiscordMessageAttachment("image.png", resource, config.isForceJoinServerDMAttachment())) //
                .map(List::of) //
                .orElse(null); //

        final var message = templateEngine.process(variables);

        final var userIdStr = String.valueOf(userId);
        final MessageCreateRequest messageRequest = MessageCreateRequest.builder() //
                .content(message) //
                .allowedMentions(AllowedMentionsData.builder() //
                        .addUser(userIdStr) //
                        .build() //
                ).build();

        discordDMService.scheduleDM(userIdStr, messageRequest, attachments);
    }
}
