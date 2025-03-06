package org.techuni.TechUniInviteSystem.controller.view.invite;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Value;
import org.springframework.web.servlet.view.RedirectView;

@EqualsAndHashCode(callSuper = false)
@Value
@AllArgsConstructor
public class DiscordJoinSuccessView extends RedirectView implements IInviteAcceptView {

    private final static String CHANNEL_PAGE_TEMPLATE = "https://discord.com/channels/%s";

    public DiscordJoinSuccessView(final String guildId) {
        super(String.format(CHANNEL_PAGE_TEMPLATE, guildId));
    }

}
