package org.techuni.TechUniInviteSystem.controller;

import java.util.function.Supplier;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
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
import org.techuni.TechUniInviteSystem.external.discord.DiscordAPIFactory;
import org.techuni.TechUniInviteSystem.service.DiscordAPIService;
import org.techuni.TechUniInviteSystem.service.InviteService;

@Controller
@RequestMapping("/discord")
@AllArgsConstructor
public class DiscordController {

    private final DiscordAPIFactory discordAPIFactory;
    private final DiscordAPIService discordAPIService;
    private final InviteService inviteService;

    @GetMapping("/authenticated")
    public IInviteAcceptResponse handleAuthenticatedResponse( //
            @RequestParam(value = "code", required = false) final String code, @RequestParam(value = "state", required = false) final String state,
            @RequestParam(value = "error", required = false) final String error) {
        if (StringUtils.isNotBlank(error)) {
            if (error.equals("access_denied")) {
                throw ErrorCode.DISCORD_LOGIN_DENIED.exception();
            }
            throw ErrorCode.DISCORD_LOGIN_FAILED.exception();
        } else if (StringUtils.isBlank(code) || StringUtils.isBlank(state)) {
            throw ErrorCode.DISCORD_LOGIN_UNEXPECTED_ERROR.exception();
        }

        final var _inviteDto = inviteService.getInviteByState(state);
        if (_inviteDto.isEmpty()) {
            throw ErrorCode.INVITATION_NOT_FOUND.exception();
        }
        final var inviteDto = _inviteDto.get();

        if (!inviteDto.isEnable()) {
            throw ErrorCode.INVITATION_INVALID.exception();
        }

        final var api = discordAPIFactory.createAPI(code);

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
