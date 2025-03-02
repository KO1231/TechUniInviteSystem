package org.techuni.TechUniInviteSystem.controller.response.invite;

import java.time.ZonedDateTime;
import java.util.Map;
import java.util.UUID;
import org.techuni.TechUniInviteSystem.domain.invite.TargetApplication;
import org.techuni.TechUniInviteSystem.domain.invite.models.AbstractInviteModel;

public abstract class AbstractInviteResponse {

    public static AbstractInviteModel of(int dbId, UUID invitationCode, String searchId, boolean isEnable, TargetApplication targetApplication,
            ZonedDateTime expiresAt, Map<String, Object> data) {
        throw new UnsupportedOperationException("Not implemented.");
    }
}
