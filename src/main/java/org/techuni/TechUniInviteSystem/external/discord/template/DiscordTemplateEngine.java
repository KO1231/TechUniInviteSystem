package org.techuni.TechUniInviteSystem.external.discord.template;

import org.springframework.stereotype.Component;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

@Component
public class DiscordTemplateEngine {

    private final SpringTemplateEngine templateEngine;

    public DiscordTemplateEngine() {
        var resolver = new ClassLoaderTemplateResolver();
        resolver.setTemplateMode(TemplateMode.TEXT);
        resolver.setPrefix("templates/discord/message/");
        resolver.setSuffix(".md");
        resolver.setCharacterEncoding("UTF-8");
        resolver.setCacheable(true);

        var engine = new SpringTemplateEngine();
        engine.setTemplateResolver(resolver);
        this.templateEngine = engine;
    }

    public String process(IDiscordMessageVariables variable) {
        final var templateType = MessageType.getTypeFromVariableClass(variable.getClass());
        return this.templateEngine.process(templateType.getTemplateName(), variable.intoContext());
    }
}
