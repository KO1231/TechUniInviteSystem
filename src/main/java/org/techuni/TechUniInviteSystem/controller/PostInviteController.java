package org.techuni.TechUniInviteSystem.controller;

import jakarta.validation.constraints.NotNull;
import java.time.ZoneId;
import java.util.function.Supplier;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.techuni.TechUniInviteSystem.controller.request.invite.CreateDiscordInviteRequest;
import org.techuni.TechUniInviteSystem.domain.invite.InviteDto;
import org.techuni.TechUniInviteSystem.security.UserAuthority;
import org.techuni.TechUniInviteSystem.service.InviteService;
import org.techuni.TechUniInviteSystem.util.AuthorityUtil;

@RestController
@RequestMapping("/new")
@AllArgsConstructor
public class PostInviteController {

    private final ZoneId zoneId;
    private final InviteService inviteService;

    @PostMapping
    public void handlePostInvite(@Validated @NotNull @RequestBody final CreateDiscordInviteRequest request) {
        final var dto = InviteDto.fromRequest(request, zoneId);
        inviteService.createInvite(dto);
    }

    public static AuthorizationDecision check(Supplier<Authentication> _authentication, RequestAuthorizationContext object) {
        final var authentication = _authentication.get();
        final var method = object.getRequest().getMethod();

        if (method.equals(HttpMethod.POST.name())) {
            return new AuthorizationDecision( //
                    AuthorityUtil.hasAuthority(authentication.getAuthorities(), UserAuthority.INVITE_WRITE) //
            );
        }

        // その他のリクエストはdenyAll
        return new AuthorizationDecision(false);
    }

}
