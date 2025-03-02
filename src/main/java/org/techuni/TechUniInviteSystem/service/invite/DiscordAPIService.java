package org.techuni.TechUniInviteSystem.service.invite;

import discord4j.discordjson.json.AuthorizationCodeGrantRequest;
import discord4j.oauth2.DiscordOAuth2Client;
import discord4j.rest.RestClient;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.techuni.TechUniInviteSystem.config.DiscordConfig;
import org.techuni.TechUniInviteSystem.external.discord.DiscordAPI;

@Service
@AllArgsConstructor
public class DiscordAPIService {

    private final RestClient restClient;
    private final long clientId;
    private final String clientSecret;
    private final String redirectUri;

    @Autowired
    public DiscordAPIService(final DiscordConfig discordConfig) {
        this.restClient = RestClient.create(discordConfig.getToken());
        this.clientId = Long.parseLong(discordConfig.getClientId());
        this.clientSecret = discordConfig.getClientSecret();
        this.redirectUri = discordConfig.getAuthenticatedEndpoint();
    }

    public DiscordAPI createAPI(final String exchangeCode) {
        final var oauth2client = DiscordOAuth2Client.createFromCode(restClient, AuthorizationCodeGrantRequest.builder() //
                .clientId(clientId) //
                .clientSecret(clientSecret) //
                .code(exchangeCode) //
                .redirectUri(redirectUri) //
                .build());

        return new DiscordAPI(oauth2client);
    }
}
