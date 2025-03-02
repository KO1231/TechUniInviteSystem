package org.techuni.TechUniInviteSystem.service.invite;

import lombok.AllArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.techuni.TechUniInviteSystem.config.DiscordConfig;
import org.techuni.TechUniInviteSystem.controller.response.invite.DiscordAuthRequestResponse;
import org.techuni.TechUniInviteSystem.db.repository.DiscordInviteRepository;
import org.techuni.TechUniInviteSystem.domain.invite.InviteDto;
import org.techuni.TechUniInviteSystem.domain.invite.models.DiscordInviteModel;

@Service
@AllArgsConstructor
public class DiscordInviteService extends AbstractInviteService {

    private final static int STATE_LENGTH = 255;

    private final DiscordInviteRepository discordInviteRepository;

    private final String clientId;
    private final String authenticatedEndpoint;

    @Autowired
    public DiscordInviteService(final DiscordConfig config, final DiscordInviteRepository discordInviteRepository) {
        this.discordInviteRepository = discordInviteRepository;
        this.clientId = config.getClientId();
        this.authenticatedEndpoint = config.getAuthenticatedEndpoint();
    }

    @Override
    public DiscordAuthRequestResponse acceptInvite(InviteDto inviteDto) {
        final var invite = inviteDto.intoModel(DiscordInviteModel.class);
        final var state = RandomStringUtils.secureStrong() //
                .nextAlphanumeric(STATE_LENGTH);

        discordInviteRepository.addInviteState(invite.getDbId(), state);

        return new DiscordAuthRequestResponse(clientId, authenticatedEndpoint, state);
    }


}
