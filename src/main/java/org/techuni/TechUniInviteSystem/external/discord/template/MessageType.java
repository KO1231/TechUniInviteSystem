package org.techuni.TechUniInviteSystem.external.discord.template;

import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.techuni.TechUniInviteSystem.error.ErrorCode;
import org.techuni.TechUniInviteSystem.external.discord.template.variables.JoinServerDMVariable;

@AllArgsConstructor
public enum MessageType {
    JOIN_SERVER_DM("join-server", JoinServerDMVariable.class),
    ;

    @Getter
    private final String templateName;
    private final Class<? extends IDiscordMessageVariables> variableClass;

    public static MessageType getTypeFromVariableClass(Class<? extends IDiscordMessageVariables> targetClass) {
        return Arrays.stream(MessageType.values()) //
                .filter(t -> t.variableClass.equals(targetClass)) //
                .findFirst() //
                .orElseThrow(() -> ErrorCode.UNEXPECTED_ERROR.exception("Could not find MessageType from variableClass: " + targetClass.getName()));
    }
}
