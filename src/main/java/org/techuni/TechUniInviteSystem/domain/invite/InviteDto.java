package org.techuni.TechUniInviteSystem.domain.invite;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.techuni.TechUniInviteSystem.controller.response.invite.AbstractInviteResponse;
import org.techuni.TechUniInviteSystem.db.entity.base.Invite;
import org.techuni.TechUniInviteSystem.domain.invite.models.AbstractInviteModel;
import org.techuni.TechUniInviteSystem.domain.invite.models.additional.AbstractInviteAdditionalData;

public record InviteDto(int dbId, UUID invitationCode, String searchId, boolean isEnable, TargetApplication targetApplication,
        ZonedDateTime expiresAt, Map<String, Object> data) {

    public InviteDto(int dbId, String invitationCode, String searchId, boolean isEnable, TargetApplication targetApplication, ZonedDateTime expiresAt,
            Map<String, Object> data) {
        this(dbId, UUID.fromString(invitationCode), searchId, isEnable, targetApplication, expiresAt, data);
    }

    public static InviteDto fromDB(final Invite invite, final ZoneId zone, final Map<String, Object> additionalData) {
        final var expiresAt = Optional.ofNullable(invite.getExpiresAt()).map(t -> t.atZone(zone));
        final boolean isEnable = !invite.getIsDisabled() && !invite.getIsUsed() && //
                expiresAt.map(t -> t.isAfter(ZonedDateTime.now(zone))).orElse(true);

        return new InviteDto(invite.getId(), invite.getCode(), invite.getSearchId(), isEnable, TargetApplication.getById(invite.getTargetAppId()),
                expiresAt.orElse(null), additionalData);
    }

    public <T extends AbstractInviteAdditionalData> AbstractInviteModel<T> intoModel() {
        final var modelClass = targetApplication.getModelClass();
        final Method ofMethod;
        try {
            ofMethod = modelClass.getMethod("of", int.class, UUID.class, String.class, boolean.class, TargetApplication.class, ZonedDateTime.class,
                    Map.class);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Cannot find of method in model class", e);
        }

        try {
            @SuppressWarnings("unchecked") //
            final var model = (AbstractInviteModel<T>) ofMethod.invoke(modelClass, dbId, invitationCode, searchId, isEnable, targetApplication,
                    expiresAt, data);

            return model;
        } catch (InvocationTargetException | IllegalAccessException | ClassCastException e) {
            throw new RuntimeException(e);
        }
    }

    public <M extends AbstractInviteModel<?>> M intoModel(Class<M> modelClass) {
        final var _modelClass = targetApplication.getModelClass();
        if (!modelClass.equals(_modelClass)) {
            throw new IllegalArgumentException("Model class is not matched.");
        }

        return modelClass.cast(intoModel());
    }

    public <R extends AbstractInviteResponse> R intoResponse(final Class<R> responseClazz) {
        final Method ofMethod;
        try {
            ofMethod = responseClazz.getMethod("of", int.class, UUID.class, String.class, boolean.class, TargetApplication.class, ZonedDateTime.class,
                    AbstractInviteAdditionalData.class);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Cannot find of method in response class", e);
        }

        final AbstractInviteResponse response;
        try {
            response = (AbstractInviteResponse) ofMethod.invoke(null, dbId, invitationCode, searchId, isEnable, targetApplication, expiresAt, data);
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        return responseClazz.cast(response);
    }

}
