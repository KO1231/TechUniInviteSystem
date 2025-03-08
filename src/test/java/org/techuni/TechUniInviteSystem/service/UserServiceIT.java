package org.techuni.TechUniInviteSystem.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.techuni.TechUniInviteSystem.db.TestUser;
import org.techuni.TechUniInviteSystem.security.UserAuthority;
import org.techuni.TechUniInviteSystem.type.AbstractIntegrationTest;

public class UserServiceIT extends AbstractIntegrationTest {

    @Autowired
    private UserService userService;

    @Test
    void 正_ユーザーを取得できる() throws Exception {
        final var expected = registerUser(TestUser.builder("sample-user-123") //
                .authorities(Set.of(UserAuthority.INVITE_WRITE)) //
                .build());

        // execute
        final var result = userService.getUserByName(expected.getName(), false);
        assertThat(result.isPresent()).isTrue();

        final var user = result.get().intoModel();
        assertThat(user).isEqualTo(expected);
    }

    @Test
    void 正_無効なユーザーをフィルタできる() throws Exception {
        final var expected = registerUser(TestUser.builder("sample-user-disable") //
                .isEnable(false) //
                .build());
        // execute
        final var result = userService.getUserByName(expected.getName(), true);
        assertThat(result.isEmpty()).isTrue();
    }

    @Test
    void 異_存在しないユーザー名で取得できない() throws Exception {
        clearUsers();

        // execute
        assertThat(userService.getUserByName("notfound-user-name", true).isEmpty()).isTrue();
        assertThat(userService.getUserByName("notfound-user-name", false).isEmpty()).isTrue();
    }
}
