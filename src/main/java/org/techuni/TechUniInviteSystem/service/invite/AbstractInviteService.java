package org.techuni.TechUniInviteSystem.service.invite;

import org.techuni.TechUniInviteSystem.controller.view.invite.IInviteAcceptView;
import org.techuni.TechUniInviteSystem.domain.invite.InviteDto;

public abstract class AbstractInviteService {

    public abstract IInviteAcceptView acceptInvite(InviteDto inviteDto);

    public abstract InviteDto createInvite(InviteDto inviteDto);
}
