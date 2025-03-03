package org.techuni.TechUniInviteSystem.controller.request.invite;

import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Value;
import lombok.experimental.SuperBuilder;
import org.hibernate.validator.constraints.Length;

@SuperBuilder
@Value
@EqualsAndHashCode(callSuper = true)
public class CreateDiscordInviteRequest extends CreateInviteRequest {

    @NotNull
    long guildId;

    @Length(max = 32)
    String nickname;

}
