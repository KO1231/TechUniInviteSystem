package org.techuni.TechUniInviteSystem.external.discord;

import discord4j.discordjson.json.AuthorizationCodeGrantRequest;
import discord4j.oauth2.DiscordOAuth2Client;
import discord4j.rest.RestClient;
import discord4j.rest.http.client.ClientException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.techuni.TechUniInviteSystem.config.DiscordConfig;
import org.techuni.TechUniInviteSystem.error.ErrorCode;

@Component
@AllArgsConstructor
public class DiscordAPIFactory {

    private final RestClient restClient;
    private final long clientId;
    private final String clientSecret;
    private final String redirectUri;

    @Autowired
    public DiscordAPIFactory(final DiscordConfig discordConfig, final RestClient restClient) {
        this.restClient = restClient;
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

        try {
            return new DiscordAPI(restClient, oauth2client);
        } catch (ClientException e) {
            throw ErrorCode.DISCORD_LOGIN_FAILED.exception(e);
        } catch (Exception e) {
            throw ErrorCode.DISCORD_LOGIN_UNEXPECTED_ERROR.exception(e);
        }
    }
}
