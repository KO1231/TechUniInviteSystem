package org.techuni.TechUniInviteSystem.service.invite;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.DefaultUriBuilderFactory.EncodingMode;
import org.springframework.web.util.UriBuilder;
import org.techuni.TechUniInviteSystem.config.DiscordConfig;
import org.techuni.TechUniInviteSystem.db.repository.DiscordInviteRepository;
import org.techuni.TechUniInviteSystem.domain.invite.InviteDto;
import org.techuni.TechUniInviteSystem.domain.invite.models.DiscordInviteModel;

@Service
@AllArgsConstructor
public class DiscordInviteService extends AbstractInviteService {

    private final static String ENDPOINT_OAUTH = "https://discord.com/api/oauth2/authorize";
    private final List<String> SCOPE = List.of("identify", "guilds", "guilds.join");
    private final static int STATE_LENGTH = 200;

    private final DiscordInviteRepository discordInviteRepository;

    private final String clientId;
    private final UriBuilder authRequestBuilder;

    @Autowired
    public DiscordInviteService(final DiscordConfig config, final DiscordInviteRepository discordInviteRepository) {
        this.discordInviteRepository = discordInviteRepository;
        this.clientId = config.getClientId();

        final var authRequestFactory = new DefaultUriBuilderFactory(ENDPOINT_OAUTH);
        authRequestFactory.setEncodingMode(EncodingMode.VALUES_ONLY);
        this.authRequestBuilder = authRequestFactory.builder() //
                .queryParam("client_id", this.clientId) //
                .queryParam("response_type", "code") //
                .queryParam("redirect_uri", URLEncoder.encode(config.getClientEndpoint(), StandardCharsets.UTF_8)) //
                .queryParam("scope", String.join("+", SCOPE)) //
                .queryParam("state", "{STATE}");
    }

    @Override
    public String acceptInvite(InviteDto inviteDto) {
        final var invite = inviteDto.intoModel(DiscordInviteModel.class);
        final var state = RandomStringUtils.secureStrong() //
                .nextAlphanumeric(STATE_LENGTH);

        discordInviteRepository.addInviteState(invite.getDbId(), state);
        return "redirect:" + getOAuthUri(state);
    }


    private URI getOAuthUri(final String id) {
        final var varMap = new HashMap<String, String>();

        varMap.put("STATE", id);

        return authRequestBuilder.build(varMap);
    }

}
