package org.techuni.TechUniInviteSystem.external.discord;

import discord4j.discordjson.Id;
import discord4j.discordjson.json.GuildMemberAddRequest;
import discord4j.discordjson.json.MemberData;
import discord4j.discordjson.json.UserData;
import discord4j.discordjson.json.UserGuildData;
import discord4j.oauth2.DiscordOAuth2Client;
import discord4j.rest.RestClient;
import discord4j.rest.http.client.ClientException;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Value;
import org.techuni.TechUniInviteSystem.error.ErrorCode;

@Value
@Getter(AccessLevel.PRIVATE)
public class DiscordAPI {

    RestClient restClient;
    DiscordOAuth2Client client;
    UserData user;
    long userId;

    public DiscordAPI(final RestClient restClient, final DiscordOAuth2Client client) {
        this.restClient = restClient;
        this.client = client;

        try {
            this.user = Optional.ofNullable(client.getCurrentUser().block()) //
                    .orElseThrow(() -> ErrorCode.DISCORD_LOAD_USER_INFO_FAILED.exception());
        } catch (ClientException e) {
            throw ErrorCode.DISCORD_LOAD_USER_INFO_FAILED.exception();
        } catch (Exception e) {
            throw ErrorCode.DISCORD_UNEXPECTED_ERROR.exception();
        }

        this.userId = user.id().asLong();
    }

    public boolean isGuildMember(final long guildId) {
        return getAllGuilds().contains(guildId);
    }

    private Set<Long> getAllGuilds() {
        try {
            return client.getCurrentUserGuilds(Collections.emptyMap()) //
                    .map(UserGuildData::id) //
                    .map(Id::asLong) //
                    .collect(Collectors.toSet()) //
                    .block();
        } catch (ClientException e) {
            throw ErrorCode.DISCORD_LOAD_GUILD_LIST_FAILED.exception(userString());
        } catch (Exception e) {
            throw ErrorCode.DISCORD_UNEXPECTED_ERROR.exception();
        }
    }

    public MemberData joinGuild(final long guildId) {
        final var request = GuildMemberAddRequest.builder() //
                .accessToken(client.getAccessToken().getAccessToken()) //
                .build();

        try {
            return restClient.getGuildService() //
                    .addGuildMember(guildId, userId, request) //
                    .block();
        } catch (ClientException e) {
            final var status = e.getStatus().code();
            if (status == 204) {
                // https://discord.com/developers/docs/resources/guild#add-guild-member
                throw ErrorCode.DISCORD_INVITATION_ALREADY_JOINED.exception();
            }
            throw ErrorCode.DISCORD_UNEXPECTED_ERROR.exception();
        } catch (Exception e) {
            throw ErrorCode.DISCORD_UNEXPECTED_ERROR.exception();
        }
    }

    public String userString() {
        return "USER=(name=%s, id=%s)".formatted(user.username(), user.id());
    }

    @Override
    public String toString() {
        return "DiscordAPI(%s)".formatted(userString());
    }
}
