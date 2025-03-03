package org.techuni.TechUniInviteSystem.domain.invite;

import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.techuni.TechUniInviteSystem.domain.invite.models.AbstractInviteModel;
import org.techuni.TechUniInviteSystem.domain.invite.models.DiscordInviteModel;

@AllArgsConstructor
@Getter
public enum TargetApplication {
    DISCORD(1, DiscordInviteModel.class),
    ;

    private final int id;
    private final Class<? extends AbstractInviteModel<?>> modelClass;

    public static TargetApplication getById(int id) {
        return Arrays.stream(values()).filter(targetApplication -> targetApplication.id == id).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Cannot find target application by id: " + id));
    }
}
