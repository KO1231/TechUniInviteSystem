package org.techuni.TechUniInviteSystem.service;

import lombok.AllArgsConstructor;
import lombok.Value;
import org.techuni.TechUniInviteSystem.controller.response.invite.DiscordJoinSuccessResponse;
import org.techuni.TechUniInviteSystem.domain.invite.InviteDto;
import org.techuni.TechUniInviteSystem.domain.invite.models.DiscordInviteModel;
import org.techuni.TechUniInviteSystem.error.ErrorCode;
import org.techuni.TechUniInviteSystem.external.discord.DiscordAPI;

@Value
@AllArgsConstructor
public class DiscordAPIService {

    DiscordAPI api;

    public DiscordJoinSuccessResponse joinGuild(final InviteDto inviteDto) {
        final var invite = inviteDto.intoModel(DiscordInviteModel.class);
        final var guildIdStr = invite.getGuildID();
        final var guildId = Long.parseLong(guildIdStr);

        if (api.isGuildMember(guildId)) {
            throw ErrorCode.DISCORD_INVITATION_ALREADY_JOINED.exception(String.valueOf(invite.getDbId()), invite.getInvitationCode().toString(),
                    guildIdStr, api.userString());
        }

        api.joinGuild(guildId, invite.getNickname());

        return new DiscordJoinSuccessResponse(guildIdStr);
    }
}
