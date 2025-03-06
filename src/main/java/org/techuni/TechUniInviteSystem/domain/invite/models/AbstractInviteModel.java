package org.techuni.TechUniInviteSystem.domain.invite.models;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.techuni.TechUniInviteSystem.domain.invite.InviteDto;
import org.techuni.TechUniInviteSystem.domain.invite.TargetApplication;
import org.techuni.TechUniInviteSystem.domain.invite.models.additional.AbstractInviteAdditionalData;
import org.techuni.TechUniInviteSystem.error.ErrorCode;

@Getter
@SuperBuilder
public abstract class AbstractInviteModel<ADDITIONAL extends AbstractInviteAdditionalData> {

    int dbId;
    UUID invitationCode;
    String searchId;
    boolean isDisabled;
    int used;
    int maxUsed;
    TargetApplication targetApplication;
    ZonedDateTime expiresAt;

    public InviteDto intoDto() {
        return new InviteDto(dbId, invitationCode.toString(), searchId, isDisabled, used, maxUsed, targetApplication, expiresAt, getAdditionalData());
    }

    public boolean isUsed() {
        return used >= maxUsed;
    }

    public boolean isEnable(ZonedDateTime time) {
        return !isDisabled && !isUsed() && Optional.ofNullable(expiresAt).map(time::isAfter).orElse(true);
    }

    public boolean isEnable(ZoneId zoneId) {
        return this.isEnable(ZonedDateTime.now(zoneId));
    }

    public boolean isDBRegistered() {
        return dbId > 0;
    }

    protected ADDITIONAL getAdditionalData() {
        // lombok で override想定
        return null;
    }

    public static <ADDITIONAL extends AbstractInviteAdditionalData> AbstractInviteModel<ADDITIONAL> of(int dbId, UUID invitationCode, String searchId,
            boolean isDisabled, int used, int maxUsed, TargetApplication targetApplication, ZonedDateTime expiresAt, ADDITIONAL data) {
        throw ErrorCode.UNEXPECTED_ERROR.exception("Not implemented (AbstractInviteModel::of).");
    }
}
