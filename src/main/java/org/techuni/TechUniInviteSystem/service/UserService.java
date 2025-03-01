package org.techuni.TechUniInviteSystem.service;

import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.techuni.TechUniInviteSystem.db.UserRepository;
import org.techuni.TechUniInviteSystem.domain.user.UserDto;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public Optional<UserDto> getUserByName(String name, boolean onlyEnable) {
        return userRepository.getUserByName(name) //
                .filter(userEntity -> !onlyEnable || userEntity.isEnable());
    }

    public Optional<UserDto> getUserByName(String name) {
        return getUserByName(name, true);
    }
}
