package org.techuni.TechUniInviteSystem.domain.invite;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.ZonedDateTime;
import java.util.Map;
import java.util.UUID;
import org.techuni.TechUniInviteSystem.domain.invite.models.AbstractInviteModel;

public record InviteDto(int dbId, UUID invitationCode, String searchId, boolean isEnable, TargetApplication targetApplication,
        ZonedDateTime expiresAt, Map<String, Object> data) {

    public InviteDto(int dbId, String invitationCode, String searchId, boolean isEnable, TargetApplication targetApplication, ZonedDateTime expiresAt,
            Map<String, Object> data) {
        this(dbId, UUID.fromString(invitationCode), searchId, isEnable, targetApplication, expiresAt, data);
    }

    public AbstractInviteModel intoModel() {
        final var modelClass = targetApplication.getModelClass();
        final Method ofMethod;
        try {
            ofMethod = modelClass.getMethod("of", int.class, UUID.class, String.class, boolean.class, TargetApplication.class, ZonedDateTime.class,
                    Map.class);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Cannot find of method in model class", e);
        }

        final AbstractInviteModel model;
        try {
            model = (AbstractInviteModel) ofMethod.invoke(modelClass, dbId, invitationCode, searchId, isEnable, targetApplication, expiresAt, data);
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        return model;
    }

}
