package org.techuni.TechUniInviteSystem.controller.response.invite;

import lombok.AllArgsConstructor;
import lombok.Value;
import org.techuni.TechUniInviteSystem.controller.view.invite.DiscordJoinSuccessView;

@Value
@AllArgsConstructor
public class DiscordJoinSuccessResponse {

    Long guildId;

    public DiscordJoinSuccessView intoView() {
        return new DiscordJoinSuccessView(guildId.toString());
    }

}
