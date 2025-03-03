package org.techuni.TechUniInviteSystem.controller.response.invite;

import java.time.ZonedDateTime;
import java.util.UUID;
import org.techuni.TechUniInviteSystem.domain.invite.TargetApplication;
import org.techuni.TechUniInviteSystem.domain.invite.models.AbstractInviteModel;
import org.techuni.TechUniInviteSystem.domain.invite.models.additional.AbstractInviteAdditionalData;

public abstract class AbstractInviteResponse {

    public static <ADDITIONAL extends AbstractInviteAdditionalData> AbstractInviteModel<ADDITIONAL> of(int dbId, UUID invitationCode, String searchId,
            boolean isEnable, TargetApplication targetApplication, ZonedDateTime expiresAt, ADDITIONAL data) {
        throw new UnsupportedOperationException("Not implemented.");
    }
}
