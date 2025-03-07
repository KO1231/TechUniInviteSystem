package org.techuni.TechUniInviteSystem.controller.request.invite;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Value;
import lombok.experimental.SuperBuilder;
import org.apache.commons.lang3.StringUtils;
import org.techuni.TechUniInviteSystem.domain.invite.models.additional.AbstractInviteAdditionalData;
import org.techuni.TechUniInviteSystem.domain.invite.models.additional.DiscordInviteAdditionalData;

@SuperBuilder
@Value
@EqualsAndHashCode(callSuper = true)
public class CreateDiscordInviteRequest extends AbstractCreateInviteRequest {

    @NotNull
    long guildId;

    @Size(max = 32)
    String nickname;

    @JsonCreator
    public CreateDiscordInviteRequest(@JsonProperty("invite") CreateInviteRequest invite, @JsonProperty("guildId") long guildId,
            @JsonProperty("nickname") String nickname) {
        super(invite);
        this.guildId = guildId;
        this.nickname = nickname;
    }

    @Override
    public AbstractInviteAdditionalData generateAdditionalData() {
        return new DiscordInviteAdditionalData(String.valueOf(guildId), StringUtils.isBlank(nickname) ? null : nickname);
    }
}
