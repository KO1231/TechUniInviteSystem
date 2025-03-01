package org.techuni.TechUniInviteSystem.controller;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.techuni.TechUniInviteSystem.db.repository.InviteRepository;
import org.techuni.TechUniInviteSystem.domain.invite.models.AbstractInviteModel;
import org.techuni.TechUniInviteSystem.error.ErrorCode;

@RestController
@RequestMapping("/invite")
@AllArgsConstructor
public class InviteController {

    private final InviteRepository inviteRepository;

    @GetMapping("/{inviteCode}")
    public AbstractInviteModel acceptInvite(@PathVariable("inviteCode") @NotNull String inviteCode) {
        if (StringUtils.isBlank(inviteCode)) {
            throw ErrorCode.INVITATION_CODE_VALIDATION_ERROR.exception();
        }
        try {
            UUID.fromString(inviteCode);
        } catch (IllegalArgumentException e) {
            throw ErrorCode.INVITATION_CODE_VALIDATION_ERROR.exception();
        }

        return inviteRepository.getInviteByCode(inviteCode).intoModel();
    }
}
