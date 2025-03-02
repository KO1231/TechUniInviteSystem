package org.techuni.TechUniInviteSystem.controller;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import java.util.function.Supplier;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpMethod;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.techuni.TechUniInviteSystem.error.ErrorCode;
import org.techuni.TechUniInviteSystem.security.UserAuthority;
import org.techuni.TechUniInviteSystem.service.InviteService;
import org.techuni.TechUniInviteSystem.util.AuthorityUtil;

@Controller
@RequestMapping("/")
@AllArgsConstructor
public class InviteController {

    private final InviteService inviteService;

    //TODO エラーをruntimeにしている
    //TODO responseの型を検討する
    @GetMapping("/accept/{inviteCode}")
    public String acceptInvite(@PathVariable("inviteCode") @NotNull String inviteCode) { // TODO modelandviewにする
        if (StringUtils.isBlank(inviteCode)) {
            throw ErrorCode.INVITATION_CODE_VALIDATION_ERROR.exception();
        }
        try {
            UUID.fromString(inviteCode);
        } catch (IllegalArgumentException e) {
            throw ErrorCode.INVITATION_CODE_VALIDATION_ERROR.exception();
        }

        final var inviteDto = inviteService.getInviteByCode(inviteCode);
        if (inviteDto.isEmpty()) {
            throw ErrorCode.INVITATION_NOT_FOUND.exception();
        }

        return inviteService.acceptInvite(inviteDto.get());
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

        // その他のリクエストは認証(ログイン)されていたらOK(特定の権限は不要)
        return AuthorityUtil.isAuthenticated(authentication);
    }
}
