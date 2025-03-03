package org.techuni.TechUniInviteSystem.service;

import discord4j.discordjson.json.AllowedMentionsData;
import discord4j.discordjson.json.MemberData;
import discord4j.discordjson.json.MessageCreateRequest;
import discord4j.rest.util.MultipartRequest;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.techuni.TechUniInviteSystem.controller.response.invite.DiscordJoinSuccessResponse;
import org.techuni.TechUniInviteSystem.domain.invite.InviteDto;
import org.techuni.TechUniInviteSystem.domain.invite.models.DiscordInviteModel;
import org.techuni.TechUniInviteSystem.error.ErrorCode;
import org.techuni.TechUniInviteSystem.error.MyHttpException;
import org.techuni.TechUniInviteSystem.external.discord.DiscordAPI;
import org.techuni.TechUniInviteSystem.external.discord.template.DiscordTemplateEngine;
import org.techuni.TechUniInviteSystem.external.discord.template.IDiscordMessageVariables;
import org.techuni.TechUniInviteSystem.external.discord.template.variables.JoinServerDMVariable;
import org.techuni.TechUniInviteSystem.service.invite.DiscordInviteService;
import reactor.util.function.Tuple2;

@Service
@Slf4j
@AllArgsConstructor
public class DiscordAPIService {

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
            sendJoinGuildDM(userId, dmVariable, null); //TODO 画像
        } catch (Exception e) {
            log.error("Some error occurred while sending DM to user. (JoinServerDM)", e);
        }

        return new DiscordJoinSuccessResponse(guildIdStr);
    }

    public void sendJoinGuildDM(long userId, IDiscordMessageVariables variables, List<Tuple2<String, InputStream>> attachments) {
        final var message = templateEngine.process(variables);

        final var userIdStr = String.valueOf(userId);
        final MultipartRequest<MessageCreateRequest> messageRequest = MultipartRequest.ofRequestAndFiles( //
                MessageCreateRequest.builder().content(message) //
                        .allowedMentions(AllowedMentionsData.builder().addUser(userIdStr).build()).build(), //
                Optional.ofNullable(attachments).orElse(Collections.emptyList()));

        discordDMService.scheduleDM(userIdStr, messageRequest);
    }
}
