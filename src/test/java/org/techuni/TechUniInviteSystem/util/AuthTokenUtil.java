package org.techuni.TechUniInviteSystem.util;

import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;
import org.techuni.TechUniInviteSystem.domain.user.UserModel;
import org.techuni.TechUniInviteSystem.security.JwtTokenProvider;

@Component
@AllArgsConstructor
public class AuthTokenUtil {

    private final JwtTokenProvider tokenProvider;

    public String generateToken(UserModel model) {
        final var userDetails = model.intoSpringUser();
        final var authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        return tokenProvider.generateToken(authentication);
    }
}
