package org.techuni.TechUniInviteSystem.service;

import discord4j.discordjson.json.DMCreateRequest;
import discord4j.discordjson.json.MessageCreateRequest;
import discord4j.rest.RestClient;
import discord4j.rest.util.MultipartRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class DiscordDMService {

    private final RestClient restClient;
    private final List<CreateDMData> data = Collections.synchronizedList(new ArrayList<>());

    public void scheduleDM(String userId, MultipartRequest<MessageCreateRequest> request) {
        data.add(new CreateDMData(userId, request));
    }

    @Scheduled(fixedRate = 10, timeUnit = TimeUnit.SECONDS)
    public void createDM() {
        synchronized (data) {
            final var iterator = data.iterator();
            while (iterator.hasNext()) {
                final var createDMData = iterator.next();
                iterator.remove();
                try {
                    createDM(createDMData);
                } catch (Exception e) {
                    log.error("Failed to create DM", e);
                }
            }
        }
    }

    private void createDM(CreateDMData createDMData) {
        final var createRequest = DMCreateRequest.builder() //
                .recipientId(createDMData.userId()) //
                .build();

        final var channel = Objects.requireNonNull(restClient.getUserService().createDM(createRequest).block());
        restClient.getChannelService() //
                .createMessage(channel.id().asLong(), createDMData.request) //
                .block();
    }

    public record CreateDMData(String userId, MultipartRequest<MessageCreateRequest> request) {

    }
}
