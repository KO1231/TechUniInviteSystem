package org.techuni.TechUniInviteSystem.domain.invite.models.additional;

import java.util.Map;

public abstract class AbstractInviteAdditionalData {

    public abstract Map<String, Object> intoMap();

    public static AbstractInviteAdditionalData fromMap(Map<String, Object> map) {
        throw new UnsupportedOperationException("Not implemented.");
    }
}
