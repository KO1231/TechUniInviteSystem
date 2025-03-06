package org.techuni.TechUniInviteSystem.controller.response.invite;

import org.techuni.TechUniInviteSystem.controller.view.invite.IInviteAcceptView;

public abstract class AbstractInviteAcceptResponse<VIEW extends IInviteAcceptView> {

    public abstract VIEW intoView();

}
