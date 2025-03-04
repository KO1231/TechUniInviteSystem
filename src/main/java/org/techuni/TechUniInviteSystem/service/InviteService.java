package org.techuni.TechUniInviteSystem.service;

import java.time.ZoneId;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.techuni.TechUniInviteSystem.controller.response.invite.IInviteAcceptResponse;
import org.techuni.TechUniInviteSystem.db.repository.InviteRepository;
import org.techuni.TechUniInviteSystem.domain.invite.InviteDto;
import org.techuni.TechUniInviteSystem.domain.invite.TargetApplication;
import org.techuni.TechUniInviteSystem.error.ErrorCode;
import org.techuni.TechUniInviteSystem.service.invite.DiscordInviteService;

@Service
@AllArgsConstructor
public class InviteService {

    private final InviteRepository inviteRepository;
    private final DiscordInviteService discordInviteService;
    private final ZoneId zoneId;

    public Optional<InviteDto> getInviteByCode(final String code) {
        return Optional.ofNullable(inviteRepository.getInviteByCode(code));
    }

    public Optional<InviteDto> getInviteByState(final String state) {
        return Optional.ofNullable(inviteRepository.getInviteByState(state));
    }

    @Transactional
    public IInviteAcceptResponse acceptInvite(final InviteDto inviteDto) {
        final var model = inviteDto.intoModel();
        if (!model.isEnable(zoneId)) {
            throw ErrorCode.INVITATION_INVALID.exception(model.getInvitationCode().toString());
        }

        final var targetApplication = inviteDto.getTargetApplication();

        if (targetApplication.equals(TargetApplication.DISCORD)) {
            return discordInviteService.acceptInvite(inviteDto);
        }

        throw ErrorCode.UNEXPECTED_ERROR.exception("Unsupported target application. (%s)".formatted(targetApplication));
    }

    public void createInvite(final InviteDto inviteDto) {
        inviteRepository.createInvite(inviteDto);
    }

    public void useInvite(final InviteDto inviteDto) {
        inviteRepository.useInvite(inviteDto.intoModel().getDbId());
    }

    public void revertUseInvite(final InviteDto inviteDto) {
        inviteRepository.revertUseInvite(inviteDto.intoModel().getDbId());
    }

}
