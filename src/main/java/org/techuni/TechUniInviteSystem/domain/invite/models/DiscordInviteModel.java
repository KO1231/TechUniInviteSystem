package org.techuni.TechUniInviteSystem.domain.invite.models;

import java.time.ZonedDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import lombok.EqualsAndHashCode;
import lombok.Value;
import lombok.experimental.SuperBuilder;
import org.techuni.TechUniInviteSystem.domain.invite.InviteDto;
import org.techuni.TechUniInviteSystem.domain.invite.TargetApplication;


@EqualsAndHashCode(callSuper = false)
@Value
@SuperBuilder
public class DiscordInviteModel extends AbstractInviteModel {

    String guildID;

    @Override
    public InviteDto intoDto() {
        return new InviteDto(dbId, invitationCode.toString(), searchId, isEnable, targetApplication, expiresAt, Map.of("guildID", guildID));
    }

    public static AbstractInviteModel of(int dbId, UUID invitationCode, String searchId, boolean isEnable, TargetApplication targetApplication,
            ZonedDateTime expiresAt, Map<String, Object> data) {
        return DiscordInviteModel.builder() //
                .dbId(dbId) //
                .invitationCode(invitationCode) //
                .searchId(searchId) //
                .isEnable(isEnable) //
                .targetApplication(targetApplication) //
                .expiresAt(expiresAt) //
                .guildID(Optional.ofNullable(data.get("guildID")).map(String::valueOf).orElseThrow()) //
                .build();
    }
}
