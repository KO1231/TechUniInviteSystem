package org.techuni.TechUniInviteSystem.external.discord.template.variables;

import lombok.AllArgsConstructor;
import lombok.Value;
import org.techuni.TechUniInviteSystem.external.discord.template.IDiscordMessageVariables;
import org.thymeleaf.context.Context;

@Value
@AllArgsConstructor
public class JoinServerDMVariable implements IDiscordMessageVariables {

    long userId;
    String name;

    @Override
    public Context intoContext() {
        final var context = new Context();
        context.setVariable("userId", userId);
        context.setVariable("name", name);
        return context;
    }
}
