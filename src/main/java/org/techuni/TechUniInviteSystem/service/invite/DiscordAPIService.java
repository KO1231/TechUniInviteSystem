package org.techuni.TechUniInviteSystem.service.invite;

import discord4j.discordjson.json.AuthorizationCodeGrantRequest;
import discord4j.oauth2.DiscordOAuth2Client;
import discord4j.rest.RestClient;
import discord4j.rest.http.client.ClientException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.techuni.TechUniInviteSystem.config.DiscordConfig;
import org.techuni.TechUniInviteSystem.controller.response.invite.DiscordJoinSuccessResponse;
import org.techuni.TechUniInviteSystem.domain.invite.InviteDto;
import org.techuni.TechUniInviteSystem.domain.invite.models.DiscordInviteModel;
import org.techuni.TechUniInviteSystem.error.ErrorCode;
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

        try {
            return new DiscordAPI(restClient, oauth2client);
        } catch (ClientException e) {
            throw ErrorCode.DISCORD_LOGIN_FAILED.exception();
        } catch (Exception e) {
            throw ErrorCode.DISCORD_LOGIN_UNEXPECTED_ERROR.exception();
        }
    }

    public DiscordJoinSuccessResponse joinGuild(final DiscordAPI api, final InviteDto inviteDto) {
        final var invite = inviteDto.intoModel(DiscordInviteModel.class);
        final var guildIdStr = invite.getGuildID();
        final var guildId = Long.parseLong(guildIdStr);

        if (api.isGuildMember(guildId)) {
            throw ErrorCode.DISCORD_INVITATION_ALREADY_JOINED.exception(String.valueOf(invite.getDbId()), invite.getInvitationCode().toString(),
                    guildIdStr, api.userString());
        }

        api.joinGuild(guildId, null); //TODO nicknameを設定できるように

        return new DiscordJoinSuccessResponse(guildIdStr);
    }
}
