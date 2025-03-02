package org.techuni.TechUniInviteSystem.controller.response.invite;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Value;
import org.springframework.web.servlet.view.RedirectView;

@EqualsAndHashCode(callSuper = false)
@Value
@AllArgsConstructor
public class DiscordJoinSuccessResponse extends RedirectView implements IInviteAcceptResponse {

    private final static String CHANNEL_PAGE_TEMPLATE = "https://discord.com/channels/%s";

    public DiscordJoinSuccessResponse(final String guildId) {
        super(String.format(CHANNEL_PAGE_TEMPLATE, guildId));
    }

}
