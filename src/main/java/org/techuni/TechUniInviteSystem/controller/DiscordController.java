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

@Controller
@RequestMapping("/discord")
@AllArgsConstructor
public class DiscordController {

    @GetMapping("/authenticated")
    public void authenticated( //
            @Valid @NotBlank @RequestParam("code") final String code, //
            @Valid @NotBlank @RequestParam("state") final String state) {
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
