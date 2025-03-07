package org.techuni.TechUniInviteSystem.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Test用 PasswordEncoder PasswordEncoderがセキュリティ面でrounds数を上げた影響でテストの時間が長くなってしまうため
 */
@Profile("test")
@Component
public class TestPasswordEncoder {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(BCryptPasswordEncoder.BCryptVersion.$2B);
    }
}
