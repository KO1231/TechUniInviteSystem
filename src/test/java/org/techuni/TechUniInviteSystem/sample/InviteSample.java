package org.techuni.TechUniInviteSystem.sample;

import java.time.ZonedDateTime;
import java.util.UUID;
import lombok.Builder;
import lombok.Data;
import org.techuni.TechUniInviteSystem.domain.invite.InviteDto;
import org.techuni.TechUniInviteSystem.domain.invite.TargetApplication;
import org.techuni.TechUniInviteSystem.domain.invite.models.additional.AbstractInviteAdditionalData;

@Data
@Builder
public class InviteSample {

    @Builder.Default
    int dbId = -1;
    @Builder.Default
    UUID invitationCode = UUID.randomUUID();
    @Builder.Default
    String searchId = null;
    @Builder.Default
    boolean isDisabled = false;
    @Builder.Default
    int used = 0;
    @Builder.Default
    int maxUsed = 1;
    @Builder.Default
    TargetApplication targetApplication = TargetApplication.DISCORD;
    @Builder.Default
    ZonedDateTime expiresAt = null;
    @Builder.Default
    AbstractInviteAdditionalData additionalData = null;

    public static class InviteSampleBuilder {

        public InviteSampleBuilder registered() {
            this.dbId(1);
            return this;
        }

        public InviteSampleBuilder registered(int _dbId) {
            this.dbId(_dbId);
            return this;
        }

        public InviteSampleBuilder fullUsed() {
            this.used(this.maxUsed$set ? this.maxUsed$value : 1);
            return this;
        }

        public InviteSampleBuilder fullUsed(int _maxUsed) {
            this.maxUsed(_maxUsed);
            return fullUsed();
        }

        public InviteSampleBuilder disabled() {
            this.isDisabled(true);
            return this;
        }
    }

    public InviteDto intoDto() {
        return new InviteDto(dbId, invitationCode, searchId, isDisabled, used, maxUsed, targetApplication, expiresAt, additionalData);
    }

}
