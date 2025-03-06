package org.techuni.TechUniInviteSystem.service.invite;

import org.techuni.TechUniInviteSystem.controller.response.invite.AbstractInviteAcceptResponse;
import org.techuni.TechUniInviteSystem.controller.response.invite.AbstractUseInviteResponse;
import org.techuni.TechUniInviteSystem.domain.invite.InviteDto;
import org.techuni.TechUniInviteSystem.domain.invite.models.additional.AbstractUsingInviteAdditionalData;

public abstract class AbstractInviteService<USING_ADDITIONAL_DATA extends AbstractUsingInviteAdditionalData> {

    public abstract AbstractInviteAcceptResponse<?> acceptInvite(InviteDto inviteDto);

    public abstract InviteDto createInvite(InviteDto inviteDto);

    public abstract AbstractUseInviteResponse useInvite(InviteDto inviteDto, USING_ADDITIONAL_DATA usingAdditionalData);
}
