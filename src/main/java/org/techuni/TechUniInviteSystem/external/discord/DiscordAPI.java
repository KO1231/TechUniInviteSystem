package org.techuni.TechUniInviteSystem.external.discord;

import discord4j.discordjson.Id;
import discord4j.discordjson.json.UserGuildData;
import discord4j.oauth2.DiscordOAuth2Client;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Value;

@Value
@Getter(AccessLevel.PRIVATE)
public class DiscordAPI {

    DiscordOAuth2Client client;

    public boolean isGuildMember(final long guildId) {
        return getAllGuilds().contains(guildId);
    }

    private Set<Long> getAllGuilds() {
        return client.getCurrentUserGuilds(Collections.emptyMap()) //
                .map(UserGuildData::id) //
                .map(Id::asLong) //
                .collect(Collectors.toSet()) //
                .block();
    }
}
