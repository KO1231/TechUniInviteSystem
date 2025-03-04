package org.techuni.TechUniInviteSystem.config;

import discord4j.rest.RestClient;
import java.net.MalformedURLException;
import java.util.Optional;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileUrlResource;
import org.springframework.stereotype.Component;

@Component
@Configuration
@Getter
public class DiscordConfig {

    @Value("${techuni.discord.token}")
    private String token;

    @Value("${techuni.discord.client-id}")
    private String clientId;

    @Value("${techuni.discord.client-secret}")
    private String clientSecret;

    @Value("${techuni.discord.endpoint.authenticated}")
    private String authenticatedEndpoint;

    @Value("${techuni.discord.state.expirationSeconds}")
    private int stateExpirationSeconds;

    @Value("${techuni.discord.file.join-server-dm.path:#{null}}")
    private Optional<String> joinServerDMAttachmentPath;
    @Value("${techuni.discord.file.join-server-dm.force:#{false}}")
    private boolean forceJoinServerDMAttachment;

    @Bean
    public RestClient getRestService() {
        return RestClient.create(token);
    }

    public Optional<FileUrlResource> getJoinServerDMAttachment() {
        return joinServerDMAttachmentPath.map(path -> {
            try {
                return new FileUrlResource(path);
            } catch (MalformedURLException e) {
                throw new IllegalArgumentException("Invalid file path (JoinServerDMAttachment): %s".formatted(path), e);
            }
        });
    }

}
