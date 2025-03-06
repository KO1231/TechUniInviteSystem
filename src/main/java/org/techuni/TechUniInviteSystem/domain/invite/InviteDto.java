package org.techuni.TechUniInviteSystem.domain.invite;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.IntStream;
import lombok.AllArgsConstructor;
import lombok.Value;
import org.techuni.TechUniInviteSystem.controller.request.invite.AbstractCreateInviteRequest;
import org.techuni.TechUniInviteSystem.controller.response.invite.AbstractInviteResponse;
import org.techuni.TechUniInviteSystem.db.entity.base.Invite;
import org.techuni.TechUniInviteSystem.domain.invite.models.AbstractInviteModel;
import org.techuni.TechUniInviteSystem.domain.invite.models.additional.AbstractInviteAdditionalData;
import org.techuni.TechUniInviteSystem.error.ErrorCode;

@Value
@AllArgsConstructor
public class InviteDto {

    int dbId;
    UUID invitationCode;
    String searchId;
    boolean isDisabled;
    int used;
    int maxUsed;
    TargetApplication targetApplication;
    ZonedDateTime expiresAt;
    AbstractInviteAdditionalData data;

    public InviteDto(int dbId, String invitationCode, String searchId, boolean isDisabled, int used, int maxUsed, TargetApplication targetApplication,
            ZonedDateTime expiresAt, AbstractInviteAdditionalData data) {
        this(dbId, UUID.fromString(invitationCode), searchId, isDisabled, used, maxUsed, targetApplication, expiresAt, data);
    }

    public static InviteDto fromDB(final Invite invite, final ZoneId zone, final AbstractInviteAdditionalData additionalData) {
        final var expiresAt = Optional.ofNullable(invite.getExpiresAt()).map(t -> t.atZone(zone));

        return new InviteDto(invite.getId(), invite.getCode(), invite.getSearchId(), invite.getIsDisabled(), invite.getUsed(), invite.getMaxUsed(),
                TargetApplication.getById(invite.getTargetAppId()), expiresAt.orElse(null), additionalData);
    }

    public static InviteDto fromRequest(final AbstractCreateInviteRequest request, final ZoneId zone) {
        final var inviteRequest = request.getInvite();
        final var expiresAt = Optional.ofNullable(inviteRequest.getExpirationDate()).map(t -> t.withZoneSameInstant(zone));

        return new InviteDto(-1, UUID.randomUUID().toString(), inviteRequest.getSearchId(), false, 0, inviteRequest.getMaxUsed(),
                inviteRequest.getTargetApp(), expiresAt.orElse(null), request.generateAdditionalData());
    }

    public <T extends AbstractInviteAdditionalData> AbstractInviteModel<T> intoModel() {
        final var modelClass = targetApplication.getModelClass();
        final Method ofMethod;
        try {
            ofMethod = findMethod(modelClass, true, "of", int.class, UUID.class, String.class, boolean.class, int.class, int.class,
                    TargetApplication.class, ZonedDateTime.class);
        } catch (NoSuchMethodException e) {
            throw ErrorCode.UNEXPECTED_ERROR.exception(e, "Cannot find of method in model class. (Class: %s)".formatted(modelClass.getName()));
        }

        try {
            @SuppressWarnings("unchecked") //
            final var model = (AbstractInviteModel<T>) ofMethod.invoke(null, dbId, invitationCode, searchId, isDisabled, used, maxUsed,
                    targetApplication, expiresAt, data);

            return model;
        } catch (InvocationTargetException | IllegalAccessException | ClassCastException e) {
            throw ErrorCode.UNEXPECTED_ERROR.exception(e,
                    "Some error occurred while creating model instance. (Class: %s)".formatted(modelClass.getName()));
        }
    }

    public Invite intoDB() {
        final var invite = new Invite();

        if (dbId > 0) {
            invite.setId(dbId);
        }
        invite.setCode(invitationCode.toString());
        invite.setSearchId(searchId);
        invite.setIsDisabled(isDisabled);
        invite.setUsed(used);
        invite.setMaxUsed(maxUsed);
        invite.setTargetAppId(targetApplication.getId());
        invite.setExpiresAt(expiresAt.toLocalDateTime());

        return invite;
    }

    public <M extends AbstractInviteModel<?>> M intoModel(Class<M> modelClass) {
        final var _modelClass = targetApplication.getModelClass();
        if (!modelClass.equals(_modelClass)) {
            throw ErrorCode.UNEXPECTED_ERROR.exception(
                    "Model class is not matched. (Expected: %s, Selected: %s)".formatted(_modelClass.getName(), modelClass.getName()));
        }

        return modelClass.cast(intoModel());
    }

    public <R extends AbstractInviteResponse<?>> R intoResponse(final Class<R> responseClazz) {
        final Method ofMethod;
        try {
            ofMethod = findMethod(responseClazz, true, "of", int.class, UUID.class, String.class, boolean.class, int.class, int.class,
                    TargetApplication.class, ZonedDateTime.class);
        } catch (NoSuchMethodException e) {
            throw ErrorCode.UNEXPECTED_ERROR.exception(e, "Cannot find of method in response class. (Class: %s)".formatted(responseClazz.getName()));
        }

        final AbstractInviteResponse<?> response;
        try {
            response = (AbstractInviteResponse<?>) ofMethod.invoke(null, dbId, invitationCode, searchId, isDisabled, used, maxUsed, targetApplication,
                    expiresAt, data);
        } catch (InvocationTargetException | IllegalAccessException | ClassCastException e) {
            throw ErrorCode.UNEXPECTED_ERROR.exception(e,
                    "Some error occurred while creating response instance. (Class: %s)".formatted(responseClazz.getName()));
        }

        return responseClazz.cast(response);
    }

    private static Method findMethod(Class<?> clazz, boolean declared, String name, Class<?>... parameterTypes) throws NoSuchMethodException {
        final var methods = declared ? clazz.getDeclaredMethods() : clazz.getMethods();

        return Arrays.stream(methods) //
                .filter(method -> method.getName().equals(name)) //
                .filter(method -> method.getParameterCount() >= parameterTypes.length) //
                .filter(method -> IntStream.range(0, parameterTypes.length) //
                        .allMatch(i -> method.getParameterTypes()[i].equals(parameterTypes[i]))) //
                .findFirst().orElseThrow(NoSuchMethodException::new);
    }

}
