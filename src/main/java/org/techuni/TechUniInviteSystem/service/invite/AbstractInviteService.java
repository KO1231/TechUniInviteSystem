package org.techuni.TechUniInviteSystem.service.invite;

import org.techuni.TechUniInviteSystem.controller.response.invite.IInviteAcceptResponse;
import org.techuni.TechUniInviteSystem.domain.invite.InviteDto;

public abstract class AbstractInviteService {

    public abstract IInviteAcceptResponse acceptInvite(InviteDto inviteDto);
}
