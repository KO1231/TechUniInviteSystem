package org.techuni.TechUniInviteSystem.domain.invite.models;

import discord4j.rest.util.Permission;
import discord4j.rest.util.PermissionSet;
import java.time.ZonedDateTime;
import java.util.EnumSet;
import java.util.UUID;
import lombok.EqualsAndHashCode;
import lombok.Value;
import lombok.experimental.SuperBuilder;
import org.apache.commons.lang3.StringUtils;
import org.techuni.TechUniInviteSystem.domain.invite.TargetApplication;
import org.techuni.TechUniInviteSystem.domain.invite.models.additional.DiscordInviteAdditionalData;


@EqualsAndHashCode(callSuper = false)
@Value
@SuperBuilder
public class DiscordInviteModel extends AbstractInviteModel<DiscordInviteAdditionalData> {

    DiscordInviteAdditionalData additionalData;

    public PermissionSet calcNeededPermissions() {
        final var permissions = EnumSet.of(Permission.CREATE_INSTANT_INVITE);
        if (StringUtils.isNotBlank(additionalData.getNickname())) {
            permissions.add(Permission.MANAGE_NICKNAMES);
        }

        return PermissionSet.of(permissions.toArray(Permission[]::new));
    }

    public static AbstractInviteModel<DiscordInviteAdditionalData> of(int dbId, UUID invitationCode, String searchId, boolean isDisabled, int used,
            int maxUsed, TargetApplication targetApplication, ZonedDateTime expiresAt, DiscordInviteAdditionalData data) {
        return DiscordInviteModel.builder() //
                .dbId(dbId) //
                .invitationCode(invitationCode) //
                .searchId(searchId) //
                .isDisabled(isDisabled) //
                .used(used) //
                .maxUsed(maxUsed) //
                .targetApplication(targetApplication) //
                .expiresAt(expiresAt) //
                .additionalData(data) //
                .build();
    }
}
