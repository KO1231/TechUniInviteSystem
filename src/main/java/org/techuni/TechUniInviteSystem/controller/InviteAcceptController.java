package org.techuni.TechUniInviteSystem.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import java.util.UUID;
import java.util.function.Supplier;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.techuni.TechUniInviteSystem.controller.response.invite.IInviteAcceptResponse;
import org.techuni.TechUniInviteSystem.error.ErrorCode;
import org.techuni.TechUniInviteSystem.security.UserAuthority;
import org.techuni.TechUniInviteSystem.service.InviteService;
import org.techuni.TechUniInviteSystem.util.AuthorityUtil;

@Controller
@RequestMapping("/accept")
@AllArgsConstructor
public class InviteAcceptController {

    private final InviteService inviteService;

    // TODO エラーをruntimeにしている
    @GetMapping("/{inviteCode}")
    public IInviteAcceptResponse acceptInvite( //
            @Valid @NotBlank @PathVariable("inviteCode") String inviteCode) {
        try {
            UUID.fromString(inviteCode);
        } catch (IllegalArgumentException e) {
            throw ErrorCode.INVITATION_CODE_VALIDATION_ERROR.exception();
        }

        final var _inviteDto = inviteService.getInviteByCode(inviteCode);
        if (_inviteDto.isEmpty()) {
            throw ErrorCode.INVITATION_NOT_FOUND.exception();
        }
        final var inviteDto = _inviteDto.get();
        if (!inviteDto.isEnable()) {
            throw ErrorCode.INVITATION_INVALID.exception();
        }

        return inviteService.acceptInvite(inviteDto);
    }

    public static AuthorizationDecision check(Supplier<Authentication> _authentication, RequestAuthorizationContext object) {
        final var authentication = _authentication.get();
        final var method = object.getRequest().getMethod();

        if (method.equals(HttpMethod.GET.name())) {
            return new AuthorizationDecision(true); // GETはpermitAll
        } else if (method.equals(HttpMethod.POST.name())) {
            return new AuthorizationDecision( //
                    AuthorityUtil.hasAuthority(authentication.getAuthorities(), UserAuthority.INVITE_WRITE) //
            );
        }

        // その他のリクエストはdenyAll
        return new AuthorizationDecision(false);
    }
}
