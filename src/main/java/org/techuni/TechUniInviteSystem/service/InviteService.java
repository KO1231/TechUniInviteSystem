package org.techuni.TechUniInviteSystem.service;

import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.techuni.TechUniInviteSystem.controller.response.invite.IInviteAcceptResponse;
import org.techuni.TechUniInviteSystem.db.repository.InviteRepository;
import org.techuni.TechUniInviteSystem.domain.invite.InviteDto;
import org.techuni.TechUniInviteSystem.domain.invite.TargetApplication;
import org.techuni.TechUniInviteSystem.service.invite.DiscordInviteService;

@Service
@AllArgsConstructor
public class InviteService {

    private final InviteRepository inviteRepository;
    private final DiscordInviteService discordInviteService;

    public Optional<InviteDto> getInviteByCode(final String code) {
        return Optional.ofNullable(inviteRepository.getInviteByCode(code));
    }

    public Optional<InviteDto> getInviteByState(final String state) {
        return Optional.ofNullable(inviteRepository.getInviteByState(state));
    }

    public IInviteAcceptResponse acceptInvite(final InviteDto inviteDto) {
        final var targetApplication = inviteDto.targetApplication();

        if (targetApplication.equals(TargetApplication.DISCORD)) {
            return discordInviteService.acceptInvite(inviteDto);
        }

        throw new IllegalArgumentException("Unsupported target application. (%s)".formatted(targetApplication));
    }

    public void useInvite(final InviteDto inviteDto) {
        inviteRepository.useInvite(inviteDto.intoModel().getDbId());
    }

}
