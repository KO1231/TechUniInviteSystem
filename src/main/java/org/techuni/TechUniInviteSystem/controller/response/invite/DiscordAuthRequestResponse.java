package org.techuni.TechUniInviteSystem.controller.response.invite;

import discord4j.oauth2.Scope;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Value;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriComponentsBuilder;

@EqualsAndHashCode(callSuper = false)
@Value
@AllArgsConstructor
public class DiscordAuthRequestResponse extends RedirectView implements IInviteAcceptResponse {

    private final static String ENDPOINT_OAUTH = "https://discord.com/api/oauth2/authorize";
    private final static List<Scope> SCOPE = List.of(Scope.IDENTIFY, Scope.GUILDS, Scope.GUILDS_JOIN);
    private final static UriBuilder authRequestBuilder = UriComponentsBuilder.fromUriString(ENDPOINT_OAUTH) //
            .queryParam("client_id", "{CLIENT_ID}") //
            .queryParam("response_type", "code") //
            .queryParam("redirect_uri", "{REDIRECT_URI}") //
            .queryParam("scope", String.join("+", SCOPE.stream().map(Scope::getValue).toList())) //
            .queryParam("state", "{STATE}");

    public DiscordAuthRequestResponse(final String clientID, final String redirectURI, final String state) {
        super(authRequestBuilder.build(getVariableMap(clientID, redirectURI, state)).toString());
    }

    private static Map<String, Object> getVariableMap(final String clientID, final String redirectURI, final String state) {
        final var varMap = new HashMap<String, Object>();

        varMap.put("CLIENT_ID", clientID);
        varMap.put("REDIRECT_URI", URI.create(redirectURI));
        varMap.put("STATE", state);

        return varMap;
    }
}
