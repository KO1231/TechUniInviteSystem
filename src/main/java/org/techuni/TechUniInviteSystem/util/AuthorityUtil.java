package org.techuni.TechUniInviteSystem.util;

import java.util.Arrays;
import java.util.Collection;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import org.springframework.security.authorization.AuthenticatedAuthorizationManager;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.techuni.TechUniInviteSystem.security.UserAuthority;

public class AuthorityUtil {

    public static boolean hasAuthority(Collection<? extends GrantedAuthority> has, UserAuthority... need) {
        return AuthorityUtils.authorityListToSet(has).containsAll(toSet(need));
    }

    public static AuthorizationDecision isAuthenticated(Supplier<Authentication> authentication) {
        return AuthenticatedAuthorizationManager.authenticated().check(authentication, null);
    }

    public static AuthorizationDecision isAuthenticated(Authentication authentication) {
        return isAuthenticated(() -> authentication);
    }

    public static Set<String> toSet(UserAuthority... authorities) {
        return authorities.length == 0 ? //
                Set.of() : //
                Arrays.stream(authorities) //
                        .map(UserAuthority::getAuthority) //
                        .collect(Collectors.toSet());
    }
}
