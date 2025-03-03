package org.techuni.TechUniInviteSystem.service;

import discord4j.discordjson.json.MemberData;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.techuni.TechUniInviteSystem.controller.response.invite.DiscordJoinSuccessResponse;
import org.techuni.TechUniInviteSystem.domain.invite.InviteDto;
import org.techuni.TechUniInviteSystem.domain.invite.models.DiscordInviteModel;
import org.techuni.TechUniInviteSystem.error.ErrorCode;
import org.techuni.TechUniInviteSystem.error.MyHttpException;
import org.techuni.TechUniInviteSystem.external.discord.DiscordAPI;
import org.techuni.TechUniInviteSystem.service.invite.DiscordInviteService;

@Service
@AllArgsConstructor
public class DiscordAPIService {

    private final InviteService inviteService;
    private final DiscordInviteService discordInviteService;

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
                inviteService.resetInviteUseStatus(inviteDto);
                throw ErrorCode.DISCORD_INVITATION_ALREADY_JOINED.exception(String.valueOf(invite.getDbId()), invite.getInvitationCode().toString(),
                        guildIdStr, api.userString());
            } else {
                throw e;
            }
        }
        discordInviteService.setJoinedUser(invite.getDbId(), memberData.user().id().asLong());

        return new DiscordJoinSuccessResponse(guildIdStr);
    }
}
