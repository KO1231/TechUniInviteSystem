package org.techuni.TechUniInviteSystem.controller.response.invite;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Value;
import org.techuni.TechUniInviteSystem.controller.view.invite.DiscordAuthRequestView;

@Value
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class DiscordAuthRequestResponse extends AbstractInviteAcceptResponse<DiscordAuthRequestView> {

    String clientID;
    String redirectURI;
    String state;

    @Override
    public DiscordAuthRequestView intoView() {
        return new DiscordAuthRequestView(clientID, redirectURI, state);
    }
}
