package org.techuni.TechUniInviteSystem.domain.invite.models;

import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.techuni.TechUniInviteSystem.domain.invite.InviteDto;
import org.techuni.TechUniInviteSystem.domain.invite.TargetApplication;
import org.techuni.TechUniInviteSystem.domain.invite.models.additional.AbstractInviteAdditionalData;

@Getter
@SuperBuilder
public abstract class AbstractInviteModel<ADDITIONAL extends AbstractInviteAdditionalData> {

    int dbId;
    UUID invitationCode;
    String searchId;
    boolean isEnable;
    TargetApplication targetApplication;
    ZonedDateTime expiresAt;

    public InviteDto intoDto() {
        return new InviteDto(dbId, invitationCode.toString(), searchId, isEnable, targetApplication, expiresAt,
                Optional.ofNullable(getAdditionalData()).map(AbstractInviteAdditionalData::intoMap).orElse(Collections.emptyMap()));
    }

    protected ADDITIONAL getAdditionalData() {
        return null;
    }

    public static <ADDITIONAL extends AbstractInviteAdditionalData> AbstractInviteModel<ADDITIONAL> of(int dbId, UUID invitationCode, String searchId,
            boolean isEnable, TargetApplication targetApplication, ZonedDateTime expiresAt, ADDITIONAL data) {
        throw new UnsupportedOperationException("Not implemented.");
    }
}
