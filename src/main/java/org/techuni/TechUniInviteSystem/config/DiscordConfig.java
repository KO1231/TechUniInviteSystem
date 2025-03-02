package org.techuni.TechUniInviteSystem.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@Configuration
@Getter
public class DiscordConfig {

    @Value("${techuni.discord.client-id}")
    private String clientId;

    @Value("${techuni.discord.client-endpoint}")
    private String clientEndpoint;

}
