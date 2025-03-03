package org.techuni.TechUniInviteSystem.error;

import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    /**
     * OTHER
     */
    UNEXPECTED_ERROR(ErrorSource.OTHER, 1, HttpStatus.INTERNAL_SERVER_ERROR, ErrorMessage.INTERNAL_UNEXPECTED_ERROR,
            ErrorMessage.UNEXPECTED_ERROR), //

    /**
     * LOGIN
     **/
    LOGIN_FAILED(ErrorSource.LOGIN, 1, HttpStatus.UNAUTHORIZED), //
    LOGIN_USER_DENIED(ErrorSource.LOGIN, 2, HttpStatus.UNAUTHORIZED, ErrorMessage.LOGIN_USER_DENIED), //
    LOGIN_UNEXCEPTED_ERROR(ErrorSource.LOGIN, 3, HttpStatus.UNAUTHORIZED, ErrorMessage.LOGIN_UNEXCEPTED_ERROR), //
    LOGIN_REQUEST_VALIDATION_ERROR(ErrorSource.LOGIN, 4, HttpStatus.UNAUTHORIZED), //

    /**
     * INVITATION
     **/
    INVITATION_NOT_FOUND(ErrorSource.INVITATION, 1, HttpStatus.NOT_FOUND, null, ErrorMessage.INVITATION_INVALID), //
    // 総当たりでnotfoundになるようにNOT_FOUNDで返す
    INVITATION_ALREADY_USED(ErrorSource.INVITATION, 2, HttpStatus.NOT_FOUND, ErrorMessage.INTERNAL_INVITATION_ALREADY_USED,
            ErrorMessage.INVITATION_INVALID), //
    INVITATION_INVALID(ErrorSource.INVITATION, 3, HttpStatus.NOT_FOUND, ErrorMessage.INTERNAL_INVITATION_INVALID, ErrorMessage.INVITATION_INVALID), //
    INVITATION_CODE_VALIDATION_ERROR(ErrorSource.INVITATION, 4, HttpStatus.NOT_FOUND, null, ErrorMessage.INVITATION_INVALID), //

    /* DISCORD INVITATION */
    DISCORD_INVITATION_ALREADY_JOINED(ErrorSource.INVITATION, 101, HttpStatus.CONFLICT, ErrorMessage.INTERNAL_DISCORD_ALREADY_JOINED,
            ErrorMessage.DISCORD_ALREADY_JOINED), //
    DISCORD_LOGIN_FAILED(ErrorSource.INVITATION, 102, HttpStatus.UNAUTHORIZED, ErrorMessage.INTERNAL_DISCORD_LOGIN_FAILED,
            ErrorMessage.DISCORD_LOGIN_FAILED), //
    DISCORD_LOGIN_UNEXPECTED_ERROR(ErrorSource.INVITATION, 103, HttpStatus.UNAUTHORIZED, ErrorMessage.INTERNAL_DISCORD_LOGIN_UNEXPECTED_ERROR,
            ErrorMessage.DISCORD_LOGIN_FAILED), //
    DISCORD_UNEXPECTED_ERROR(ErrorSource.INVITATION, 104, HttpStatus.INTERNAL_SERVER_ERROR, ErrorMessage.INTERNAL_DISCORD_UNEXPECTED_ERROR,
            ErrorMessage.DISCORD_UNEXPECTED_ERROR), //
    DISCORD_LOAD_GUILD_LIST_FAILED(ErrorSource.INVITATION, 105, HttpStatus.UNAUTHORIZED, ErrorMessage.INTERNAL_DISCORD_LOAD_GUILD_LIST_FAILED,
            ErrorMessage.DISCORD_LOGIN_FAILED), //
    DISCORD_LOAD_USER_INFO_FAILED(ErrorSource.INVITATION, 106, HttpStatus.UNAUTHORIZED, ErrorMessage.INTERNAL_DISCORD_LOAD_USER_INFO_FAILED,
            ErrorMessage.DISCORD_LOGIN_FAILED), //
    DISCORD_LOGIN_DENIED(ErrorSource.INVITATION, 107, HttpStatus.UNAUTHORIZED, ErrorMessage.INTERNAL_DISCORD_LOGIN_DENIED,
            ErrorMessage.DISCORD_LOGIN_DENIED), //
    DISCORD_AUTHENTICATED_VALIDATION_ERROR(ErrorSource.INVITATION, 108, HttpStatus.UNAUTHORIZED, null, ErrorMessage.DISCORD_LOGIN_FAILED), //
    DISCORD_CREATE_JOIN_DM_ERROR(ErrorSource.INVITATION, 109, HttpStatus.INTERNAL_SERVER_ERROR, null, ErrorMessage.DISCORD_UNEXPECTED_ERROR), //

    /**
     * VALIDATION
     **/
    VALIDATION_ERROR(ErrorSource.VALIDATION, 1, HttpStatus.BAD_REQUEST), //
    ;

    private static final int SOURCE_MARGIN = 10000;

    private final ErrorSource source;
    private final int id;
    private final HttpStatus status;

    /*
     * エラーメッセージ(内部のエラーログ等向け) ユーザー側にはMyErrorControllerでHttpStatusのreasonPhraseを返すので表示されない。
     */
    private final ErrorMessage internalMessage;

    private final ErrorMessage userOutputMessage;

    ErrorCode(ErrorSource source, int id, HttpStatus status, ErrorMessage internalMessage, ErrorMessage userOutputMessage) {
        this.source = source;
        this.id = id;
        this.status = status;
        this.internalMessage = internalMessage;
        this.userOutputMessage = userOutputMessage;
    }

    ErrorCode(ErrorSource source, int id, HttpStatus status, ErrorMessage internalMessage) {
        this.source = source;
        this.id = id;
        this.status = status;
        this.internalMessage = internalMessage;
        this.userOutputMessage = null;
    }

    ErrorCode(ErrorSource source, int id, HttpStatus status) {
        this(source, id, status, null, null);
    }

    public int getId() {
        return source.getId() * SOURCE_MARGIN + id;
    }

    public MyHttpException exception() {
        return new MyHttpException(this);
    }

    public MyHttpException exception(Throwable cause) {
        return new MyHttpException(this, cause);
    }

    public MyHttpException exception(String... internalArgs) {
        return new MyHttpException(this, null, internalArgs, null);
    }

    public MyHttpException exception(Throwable cause, String... internalArgs) {
        return new MyHttpException(this, cause, internalArgs, null);
    }

    public String getInternalMessage() {
        final var output = Optional.ofNullable(internalMessage).map(ErrorMessage::getMessage).orElse(status.getReasonPhrase());
        return "\"%s\" - ErrorCode.%s".formatted(output, this.name());
    }

    public String getInternalMessage(String... args) {
        final var output = Optional.ofNullable(internalMessage).map(ErrorMessage::getMessage).orElse(status.getReasonPhrase())
                .formatted((Object[]) args);
        return "\"%s\" - ErrorCode.%s".formatted(output, this.name());
    }

    public String getUserOutputMessage() {
        return Optional.ofNullable(userOutputMessage).map(ErrorMessage::getMessage).orElse(null);
    }

    @Getter
    @AllArgsConstructor
    private enum ErrorSource {
        OTHER(0), //
        LOGIN(1), //
        INVITATION(2), //
        VALIDATION(3);

        private final int id;
    }
}
