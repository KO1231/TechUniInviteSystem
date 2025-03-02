package org.techuni.TechUniInviteSystem;

import java.time.ZoneId;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TechUniInviteSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(TechUniInviteSystemApplication.class, args);
    }

    @Bean
    public ZoneId zoneId() {
        return ZoneId.of("Asia/Tokyo");
    }

}
