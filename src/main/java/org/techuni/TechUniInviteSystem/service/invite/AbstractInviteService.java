package org.techuni.TechUniInviteSystem.service.invite;

import org.springframework.web.servlet.view.RedirectView;
import org.techuni.TechUniInviteSystem.domain.invite.InviteDto;

public abstract class AbstractInviteService {

    public abstract RedirectView acceptInvite(InviteDto inviteDto);
}
