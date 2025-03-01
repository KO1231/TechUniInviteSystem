package org.techuni.TechUniInviteSystem.service;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MyUserDetailsService implements UserDetailsService {

    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        final var userModel = userService.getUserByName(username, false)
                .orElseThrow(() -> new UsernameNotFoundException("User(%s) not found.".formatted(username)));

        return userModel.intoModel().intoSpringUser();
    }
}
