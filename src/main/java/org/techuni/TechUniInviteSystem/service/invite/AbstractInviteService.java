package org.techuni.TechUniInviteSystem.service.invite;

import org.techuni.TechUniInviteSystem.controller.response.invite.AbstractInviteAcceptResponse;
import org.techuni.TechUniInviteSystem.domain.invite.InviteDto;

public abstract class AbstractInviteService {

    public abstract AbstractInviteAcceptResponse<?> acceptInvite(InviteDto inviteDto);

    public abstract InviteDto createInvite(InviteDto inviteDto);
}
