package org.techuni.TechUniInviteSystem;

import java.time.ZoneId;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class TechUniInviteSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(TechUniInviteSystemApplication.class, args);
    }

    @Bean
    public ZoneId zoneId() {
        return ZoneId.of("Asia/Tokyo");
    }

}
