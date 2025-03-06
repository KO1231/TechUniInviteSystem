package org.techuni.TechUniInviteSystem.service.invite;

import static java.util.Objects.isNull;

import discord4j.common.util.Snowflake;
import discord4j.rest.RestClient;
import discord4j.rest.http.client.ClientException;
import java.time.Duration;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAmount;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.techuni.TechUniInviteSystem.config.DiscordConfig;
import org.techuni.TechUniInviteSystem.controller.response.invite.DiscordAuthRequestResponse;
import org.techuni.TechUniInviteSystem.controller.response.invite.DiscordJoinSuccessResponse;
import org.techuni.TechUniInviteSystem.db.repository.DiscordInviteRepository;
import org.techuni.TechUniInviteSystem.domain.invite.InviteDto;
import org.techuni.TechUniInviteSystem.domain.invite.TargetApplication;
import org.techuni.TechUniInviteSystem.domain.invite.models.DiscordInviteModel;
import org.techuni.TechUniInviteSystem.domain.invite.models.additional.DiscordUsingInviteAddtionalData;
import org.techuni.TechUniInviteSystem.error.ErrorCode;
import org.techuni.TechUniInviteSystem.external.discord.DiscordAPIFactory;
import org.techuni.TechUniInviteSystem.external.discord.template.variables.JoinServerDMVariable;
import org.techuni.TechUniInviteSystem.service.DiscordAPIService;
import org.techuni.TechUniInviteSystem.service.DiscordDMService;

@Service
@Slf4j
@RequiredArgsConstructor
public class DiscordInviteService extends AbstractInviteService<DiscordUsingInviteAddtionalData> {

    private final static int STATE_LENGTH = 255;

    private final RestClient restClient;
    private final ZoneId zoneId;
    private final DiscordDMService discordDMService;
    private final DiscordAPIService apiService;
    private final DiscordAPIFactory discordAPIFactory;
    private final DiscordInviteRepository discordInviteRepository;

    private String clientId;
    private String authenticatedEndpoint;
    private TemporalAmount stateExpireTime;

    @Override
    @Transactional
    public DiscordAuthRequestResponse acceptInvite(InviteDto inviteDto) {
        final var invite = inviteDto.intoModel(DiscordInviteModel.class);
        final var guildId = Long.parseLong(invite.getAdditionalData().getGuildID());
        try {
            if (isNull(restClient.getSelfMember(Snowflake.of(guildId)).block())) {
                throw ErrorCode.DISCORD_GUILD_ACCESS_ERROR.exception(String.valueOf(guildId));
            }
        } catch (ClientException e) {
            throw ErrorCode.DISCORD_GUILD_ACCESS_ERROR.exception(String.valueOf(guildId));
        }

        final var state = RandomStringUtils.secureStrong() //
                .nextAlphanumeric(STATE_LENGTH);

        discordInviteRepository.addInviteState(invite.getDbId(), state);

        return new DiscordAuthRequestResponse(clientId, authenticatedEndpoint, state);
    }

    @Override
    @Transactional
    public InviteDto createInvite(InviteDto inviteDto) {
        if (!inviteDto.getTargetApplication().equals(TargetApplication.DISCORD)) {
            throw ErrorCode.UNEXPECTED_ERROR.exception("Unsupported target application. (%s)".formatted(inviteDto.getTargetApplication()));
        }
        final var model = inviteDto.intoModel(DiscordInviteModel.class);
        final var additionalData = model.getAdditionalData();
        final var guildId = Long.parseLong(additionalData.getGuildID());

        try {
            if (isNull(restClient.getSelfMember(Snowflake.of(guildId)).block())) {
                throw ErrorCode.DISCORD_CREATE_INVITE_GUILD_ACCESS_ERROR.exception(String.valueOf(guildId));
            }
        } catch (ClientException e) {
            throw ErrorCode.DISCORD_CREATE_INVITE_GUILD_ACCESS_ERROR.exception(e, String.valueOf(guildId));
        }

        discordInviteRepository.createInvite(model.getDbId(), guildId, additionalData.getNickname());

        return inviteDto;
    }

    @Transactional
    public DiscordJoinSuccessResponse useInvite(final InviteDto inviteDto, final DiscordUsingInviteAddtionalData additionalData) {
        final var invitationCode = inviteDto.getInvitationCode().toString();
        final var invite = inviteDto.intoModel(DiscordInviteModel.class);
        if (!invite.isEnable(zoneId)) {
            throw ErrorCode.INVITATION_INVALID.exception(invitationCode);
        }

        final var guildId = Long.parseLong(invite.getAdditionalData().getGuildID());
        try {
            if (isNull(restClient.getSelfMember(Snowflake.of(guildId)).block())) {
                throw ErrorCode.DISCORD_GUILD_ACCESS_ERROR.exception(String.valueOf(guildId));
            }
        } catch (ClientException e) {
            throw ErrorCode.DISCORD_GUILD_ACCESS_ERROR.exception(String.valueOf(guildId));
        }

        final var code = additionalData.getCode();
        final var api = discordAPIFactory.createAPI(code);

        // execute
        final var resultMember = apiService.joinGuild(api, invite);
        final var joinedUser = resultMember.user();
        final var joinedUserId = joinedUser.id().asLong();

        // store joined user's data
        try {
            discordInviteRepository.addJoinedUser(invite.getDbId(), joinedUserId);
        } catch (Exception e) {
            log.error("Some error occurred while setting joined user.", e);
        }

        // send DM to user
        final var dmVariable = new JoinServerDMVariable( //
                joinedUserId, //
                Optional.ofNullable(invite.getAdditionalData().getNickname()).orElse(joinedUser.username()) //
        );
        try {
            discordDMService.scheduleDM(joinedUserId, dmVariable);
        } catch (Exception e) {
            log.error("Some error occurred while scheduling DM to user. (JoinServerDM)", e);
        }

        return new DiscordJoinSuccessResponse(guildId);
    }

    // 5分ごとにstateテーブルをclean
    @Scheduled(fixedRate = 5L, timeUnit = TimeUnit.MINUTES)
    public void cleanState() {
        discordInviteRepository.cleanState(stateExpireTime);
    }

    @Autowired
    public void setClientId(final DiscordConfig config) {
        this.clientId = config.getClientId();
    }

    @Autowired
    public void setAuthenticatedEndpoint(final DiscordConfig config) {
        this.authenticatedEndpoint = config.getAuthenticatedEndpoint();
    }

    @Autowired
    public void setStateExpireTime(final DiscordConfig config) {
        this.stateExpireTime = Duration.of(config.getStateExpirationSeconds(), ChronoUnit.SECONDS);
    }
}
