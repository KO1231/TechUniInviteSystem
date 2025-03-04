package org.techuni.TechUniInviteSystem.service;

import discord4j.discordjson.json.DMCreateRequest;
import discord4j.discordjson.json.MessageCreateRequest;
import discord4j.rest.RestClient;
import discord4j.rest.util.MultipartRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import reactor.util.function.Tuples;

@Service
@Slf4j
@RequiredArgsConstructor
public class DiscordDMService {

    private final RestClient restClient;
    private final List<CreateDMData> data = Collections.synchronizedList(new ArrayList<>());

    public void scheduleDM(String userId, MessageCreateRequest request, List<DiscordMessageAttachment> attachments) {
        data.add(new CreateDMData(userId, request, Optional.ofNullable(attachments).orElse(Collections.emptyList())));
    }

    @Scheduled(fixedRate = 10, timeUnit = TimeUnit.SECONDS)
    public void sendDMScheduler() {
        synchronized (data) {
            final var iterator = data.iterator();
            while (iterator.hasNext()) {
                final var createDMData = iterator.next();
                iterator.remove();
                try {
                    sendDM(createDMData);
                } catch (Exception e) {
                    log.error("Failed to create DM", e);
                }
            }
        }
    }

    private void sendDM(CreateDMData createDMData) {
        final var createRequest = DMCreateRequest.builder() //
                .recipientId(createDMData.userId()) //
                .build();

        final var attachments = createDMData.attachments.stream() //
                .map(attachment -> {
                    try {
                        return Tuples.of(attachment.name, attachment.resource.getInputStream());
                    } catch (IOException e) {
                        if (attachment.forceLoad) { // isForceLoadAttachment
                            throw new RuntimeException("Failed to load attachment: %s".formatted(attachment.name), e);
                        } else {
                            final var message = "Failed to load attachment. But the DM will sent(because it is not forced attachment). : %s".formatted(
                                    attachment.name);
                            log.warn(message, e);
                            return null;
                        }
                    }
                }) //
                .filter(Objects::nonNull) //
                .toList();

        final var dmRequest = MultipartRequest.ofRequestAndFiles(createDMData.request, attachments);

        final var channel = Objects.requireNonNull(restClient.getUserService().createDM(createRequest).block());
        restClient.getChannelService() //
                .createMessage(channel.id().asLong(), dmRequest) //
                .block();
    }

    public record CreateDMData(String userId, MessageCreateRequest request, List<DiscordMessageAttachment> attachments) {

    }

    public record DiscordMessageAttachment(String name, Resource resource, boolean forceLoad) {

    }
}
