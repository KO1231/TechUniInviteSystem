package org.techuni.TechUniInviteSystem.service;

import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.techuni.TechUniInviteSystem.db.repository.InviteRepository;
import org.techuni.TechUniInviteSystem.domain.invite.InviteDto;

@Service
@AllArgsConstructor
public class InviteService {

    private final InviteRepository inviteRepository;

    public Optional<InviteDto> getInviteByCode(final String code) {
        return Optional.ofNullable(inviteRepository.getInviteByCode(code));
    }

    public String acceptInvite(final InviteDto inviteDto) {
        final var invite = inviteDto.intoModel();
    }

}
