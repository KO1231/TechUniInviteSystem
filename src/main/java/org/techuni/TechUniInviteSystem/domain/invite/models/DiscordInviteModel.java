package org.techuni.TechUniInviteSystem.domain.invite.models;

import java.time.ZonedDateTime;
import java.util.Map;
import java.util.UUID;
import lombok.EqualsAndHashCode;
import lombok.Value;
import lombok.experimental.SuperBuilder;
import org.techuni.TechUniInviteSystem.domain.invite.TargetApplication;
import org.techuni.TechUniInviteSystem.domain.invite.models.additional.DiscordInviteAdditionalData;


@EqualsAndHashCode(callSuper = false)
@Value
@SuperBuilder
public class DiscordInviteModel extends AbstractInviteModel<DiscordInviteAdditionalData> {

    DiscordInviteAdditionalData additionalData;

    public static AbstractInviteModel<DiscordInviteAdditionalData> of(int dbId, UUID invitationCode, String searchId, boolean isEnable,
            TargetApplication targetApplication, ZonedDateTime expiresAt, Map<String, Object> data) {
        return DiscordInviteModel.builder() //
                .dbId(dbId) //
                .invitationCode(invitationCode) //
                .searchId(searchId) //
                .isEnable(isEnable) //
                .targetApplication(targetApplication) //
                .expiresAt(expiresAt) //
                .additionalData(DiscordInviteAdditionalData.fromMap(data)).build();
    }
}
