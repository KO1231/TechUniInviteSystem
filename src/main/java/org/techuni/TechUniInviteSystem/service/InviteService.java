package org.techuni.TechUniInviteSystem.service;

import static java.util.Objects.isNull;

import java.time.ZoneId;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.techuni.TechUniInviteSystem.controller.response.invite.AbstractInviteAcceptResponse;
import org.techuni.TechUniInviteSystem.controller.view.invite.IInviteAcceptView;
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
    public IInviteAcceptView acceptInvite(final InviteDto inviteDto) {
        final var model = inviteDto.intoModel();
        if (!model.isEnable(zoneId)) {
            throw ErrorCode.INVITATION_INVALID.exception(model.getInvitationCode().toString());
        }

        final var targetApplication = inviteDto.getTargetApplication();
        AbstractInviteAcceptResponse<?> response = null;
        if (targetApplication.equals(TargetApplication.DISCORD)) {
            response = discordInviteService.acceptInvite(inviteDto);
        }

        if (isNull(response)) {
            throw ErrorCode.UNEXPECTED_ERROR.exception("Unsupported target application. (%s)".formatted(targetApplication));
        }
        return response.intoView();
    }

    @Transactional
    public void createInvite(final InviteDto inviteDto) {
        final var model = inviteDto.intoModel();
        if (model.isDBRegistered() || model.isUsed()) {
            throw ErrorCode.INVITATION_CREATE_REGISTERED_INVITE.exception(model.getInvitationCode().toString());
        }

        final var createdDto = inviteRepository.createInvite(inviteDto);

        final var targetApplication = createdDto.getTargetApplication();
        if (targetApplication.equals(TargetApplication.DISCORD)) {
            discordInviteService.createInvite(createdDto);
        }

    }

    public void useInvite(final InviteDto inviteDto) {
        inviteRepository.useInvite(inviteDto.intoModel().getDbId());
    }

    public void revertUseInvite(final InviteDto inviteDto) {
        inviteRepository.revertUseInvite(inviteDto.intoModel().getDbId());
    }

}
