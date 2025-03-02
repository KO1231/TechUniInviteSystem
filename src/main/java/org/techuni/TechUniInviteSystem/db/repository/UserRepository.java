package org.techuni.TechUniInviteSystem.db.repository;

import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import org.techuni.TechUniInviteSystem.db.mapper.UserMapper;
import org.techuni.TechUniInviteSystem.domain.user.UserDto;

@Repository
@AllArgsConstructor
public class UserRepository {

    private final UserMapper userMapper;

    public Optional<UserDto> getUserByName(String name) {
        return Optional.ofNullable(userMapper.getUserByName(name)) //
                .map(UserDto::new);
    }

}
