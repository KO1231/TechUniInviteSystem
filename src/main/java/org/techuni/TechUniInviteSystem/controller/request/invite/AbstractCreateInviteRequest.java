package org.techuni.TechUniInviteSystem.controller.request.invite;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.techuni.TechUniInviteSystem.domain.invite.models.additional.AbstractInviteAdditionalData;

@Getter
@SuperBuilder
public abstract class AbstractCreateInviteRequest {

    @Valid
    @NotNull
    protected CreateInviteRequest invite;

    protected AbstractCreateInviteRequest(CreateInviteRequest invite) {
        this.invite = invite;
    }

    public abstract AbstractInviteAdditionalData generateAdditionalData();

}
