package org.techuni.TechUniInviteSystem.domain.invite.models;

import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.techuni.TechUniInviteSystem.domain.invite.InviteDto;
import org.techuni.TechUniInviteSystem.domain.invite.TargetApplication;

@Getter
@SuperBuilder
public abstract class AbstractInviteModel {

    int dbId;
    UUID invitationCode;
    String searchId;
    boolean isEnable;
    TargetApplication targetApplication;
    ZonedDateTime expiresAt;

    public InviteDto intoDto() {
        return new InviteDto(dbId, invitationCode.toString(), searchId, isEnable, targetApplication, expiresAt, generateAdditionalData());
    }

    protected Map<String, Object> generateAdditionalData() {
        return Collections.emptyMap();
    }

    public static AbstractInviteModel of(int dbId, UUID invitationCode, String searchId, boolean isEnable, TargetApplication targetApplication,
            ZonedDateTime expiresAt, Map<String, Object> data) {
        throw new UnsupportedOperationException("Not implemented.");
    }
}
