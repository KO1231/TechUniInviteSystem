package org.techuni.TechUniInviteSystem.domain.user;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.techuni.TechUniInviteSystem.security.UserAuthority;

@Builder
@Data
public class UserModel {

    public static final List<Character> ALLOWED_PASSWORD_SPECIAL_CHARS = //
            List.of('^', '$', '*', '.', '[', ']', '{', '}', '(', ')', '?', '!', '@', '#', '%', '&', ',', '>', '<', ':', ';', '|', '_', '-', '~', '`');


    private final UUID id;

    private final long dbId;

    private final boolean isEnable;

    @NonNull
    private final String name;

    @NonNull
    private final String passHash;

    @NonNull
    private final Set<UserAuthority> authorities;

    public UserDetails intoSpringUser() {
        return User.builder() //
                .username(name) //
                .password(passHash) //
                .disabled(!isEnable) //
                .authorities(authorities.stream().map(UserAuthority::getAuthority).toArray(String[]::new)) //
                .build();
    }
}
