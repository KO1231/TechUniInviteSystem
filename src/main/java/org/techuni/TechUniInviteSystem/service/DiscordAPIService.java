package org.techuni.TechUniInviteSystem.service;

import discord4j.discordjson.json.MemberData;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.techuni.TechUniInviteSystem.domain.invite.models.DiscordInviteModel;
import org.techuni.TechUniInviteSystem.error.ErrorCode;
import org.techuni.TechUniInviteSystem.external.discord.DiscordAPI;

@Service
@Slf4j
@AllArgsConstructor
public class DiscordAPIService {

    public MemberData joinGuild(final DiscordAPI api, final DiscordInviteModel invite) {
        final var discordInvite = invite.getAdditionalData();

        final var guildIdStr = discordInvite.getGuildID();
        final var guildId = Long.parseLong(guildIdStr);

        if (api.isGuildMember(guildId)) {
            throw ErrorCode.DISCORD_INVITATION_ALREADY_JOINED.exception(String.valueOf(invite.getDbId()), invite.getInvitationCode().toString(),
                    guildIdStr, api.userString());
        }

        return api.joinGuild(guildId, discordInvite.getNickname());
    }
}
