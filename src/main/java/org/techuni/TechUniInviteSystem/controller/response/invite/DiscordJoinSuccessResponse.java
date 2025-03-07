package org.techuni.TechUniInviteSystem.controller.response.invite;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Value;
import org.techuni.TechUniInviteSystem.controller.view.invite.DiscordJoinSuccessView;

@Value
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class DiscordJoinSuccessResponse extends AbstractUseInviteResponse {

    Long guildId;

    public DiscordJoinSuccessView intoView() {
        return new DiscordJoinSuccessView(guildId.toString());
    }

}
