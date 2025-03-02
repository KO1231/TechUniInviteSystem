package org.techuni.TechUniInviteSystem.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import java.util.function.Supplier;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.techuni.TechUniInviteSystem.controller.response.invite.IInviteAcceptResponse;
import org.techuni.TechUniInviteSystem.error.ErrorCode;
import org.techuni.TechUniInviteSystem.service.InviteService;
import org.techuni.TechUniInviteSystem.service.invite.DiscordAPIService;

@Controller
@RequestMapping("/discord")
@AllArgsConstructor
public class DiscordController {

    private final InviteService inviteService;
    private final DiscordAPIService discordAPIService;

    @GetMapping("/authenticated")
    public IInviteAcceptResponse handleAuthenticatedResponse( //
            @Valid @NotBlank @RequestParam("code") final String code, //
            @Valid @NotBlank @RequestParam("state") final String state) {
        final var _inviteDto = inviteService.getInviteByState(state);
        if (_inviteDto.isEmpty()) {
            throw ErrorCode.INVITATION_NOT_FOUND.exception();
        }
        final var inviteDto = _inviteDto.get();

        final var api = discordAPIService.createAPI(code);
        return discordAPIService.joinGuild(api, inviteDto);
    }

    public static AuthorizationDecision check(Supplier<Authentication> _authentication, RequestAuthorizationContext object) {
        final var authentication = _authentication.get();
        final var method = object.getRequest().getMethod();

        if (method.equals(HttpMethod.GET.name())) {
            return new AuthorizationDecision(true); // GETはpermitAll
        }

        // その他のリクエストはdenyAll
        return new AuthorizationDecision(false);
    }
}
