package org.techuni.TechUniInviteSystem.service.invite;

import org.techuni.TechUniInviteSystem.domain.invite.InviteDto;

public abstract class AbstractInviteService {

    public abstract String acceptInvite(InviteDto inviteDto);
}
