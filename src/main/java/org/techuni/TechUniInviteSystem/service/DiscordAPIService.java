package org.techuni.TechUniInviteSystem.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.techuni.TechUniInviteSystem.controller.response.invite.DiscordJoinSuccessResponse;
import org.techuni.TechUniInviteSystem.domain.invite.InviteDto;
import org.techuni.TechUniInviteSystem.domain.invite.models.DiscordInviteModel;
import org.techuni.TechUniInviteSystem.error.ErrorCode;
import org.techuni.TechUniInviteSystem.external.discord.DiscordAPI;
import org.techuni.TechUniInviteSystem.service.invite.DiscordInviteService;

@Service
@AllArgsConstructor
public class DiscordAPIService {

    private final InviteService inviteService;
    private final DiscordInviteService discordInviteService;

    public DiscordJoinSuccessResponse joinGuild(final DiscordAPI api, final InviteDto inviteDto) {
        final var invite = inviteDto.intoModel(DiscordInviteModel.class);
        final var guildIdStr = invite.getGuildID();
        final var guildId = Long.parseLong(guildIdStr);

        if (api.isGuildMember(guildId)) {
            throw ErrorCode.DISCORD_INVITATION_ALREADY_JOINED.exception(String.valueOf(invite.getDbId()), invite.getInvitationCode().toString(),
                    guildIdStr, api.userString());
        }

        inviteService.useInvite(inviteDto);
        final var memberData = api.joinGuild(guildId, invite.getNickname());
        discordInviteService.setJoinedUser(invite.getDbId(), memberData.user().id().asLong());

        return new DiscordJoinSuccessResponse(guildIdStr);
    }
}
