package org.techuni.TechUniInviteSystem.error;

import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    /**
     * LOGIN
     **/
    LOGIN_FAILED(ErrorSource.LOGIN, 1, HttpStatus.UNAUTHORIZED), //
    LOGIN_USER_DENIED(ErrorSource.LOGIN, 2, HttpStatus.UNAUTHORIZED, "Denied user tried to login. (User: %s)"), //
    LOGIN_UNEXCEPTED_ERROR(ErrorSource.LOGIN, 3, HttpStatus.UNAUTHORIZED, "Unexpected error occurred."), //
    LOGIN_REQUEST_VALIDATION_ERROR(ErrorSource.LOGIN, 4, HttpStatus.UNAUTHORIZED), //

    /**
     * INVITATION
     **/
    INVITATION_NOT_FOUND(ErrorSource.INVITATION, 1, HttpStatus.NOT_FOUND), //
    // 総当たりでnotfoundになるようにNOT_FOUNDで返す
    INVITATION_ALREADY_USED(ErrorSource.INVITATION, 2, HttpStatus.NOT_FOUND, "Request Already Used Invitation Code. (Code: %s)"), //
    INVITATION_INVALID(ErrorSource.INVITATION, 3, HttpStatus.NOT_FOUND, "Request Invalid Invitation Code. (Code: %s)"), //
    INVITATION_CODE_VALIDATION_ERROR(ErrorSource.INVITATION, 4, HttpStatus.NOT_FOUND), //

    /* DISCORD INVITATION */
    DISCORD_INVITATION_ALREADY_JOINED(ErrorSource.INVITATION, 101, HttpStatus.CONFLICT,
            "The Discord Invitation target already joined to the guild. (DbId: %s, InvitationCode: %s, Guild: %s, User: %s)",
            "Your account have already joined to the discord server. If you think this is a mistake, please contact to the inviter."), //
    DISCORD_LOGIN_FAILED(ErrorSource.INVITATION, 102, HttpStatus.UNAUTHORIZED, "Discord login failed.",
            "Login to Your account is failed. If you think this is a mistake, please try again."), //
    DISCORD_LOGIN_UNEXPECTED_ERROR(ErrorSource.INVITATION, 103, HttpStatus.UNAUTHORIZED, "Unexpected error occurred in Discord login sequence.",
            "Login to Your account is failed. If you think this is a mistake, please try again."), //
    DISCORD_UNEXPECTED_ERROR(ErrorSource.INVITATION, 104, HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error occurred in Discord API.",
            "Unexpected error occurred in Discord API."), //
    DISCORD_LOAD_GUILD_LIST_FAILED(ErrorSource.INVITATION, 105, HttpStatus.UNAUTHORIZED, "Failed to load guild list from Discord API. (User: %s)",
            "Login to Your account is failed. If you think this is a mistake, please try again."), //
    DISCORD_LOAD_USER_INFO_FAILED(ErrorSource.INVITATION, 106, HttpStatus.UNAUTHORIZED, "Failed to load user info from Discord API.",
            "Login to Your account is failed. If you think this is a mistake, please try again."), //
    DISCORD_LOGIN_DENIED(ErrorSource.INVITATION, 107, HttpStatus.UNAUTHORIZED, "User denied to login to Discord.",
            "You denied to login to Discord. If you think this is a mistake, please try again."), //

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
    private final String internalMessage;

    @Getter
    private final String userOutputMessage;

    ErrorCode(ErrorSource source, int id, HttpStatus status, String internalMessage, String userOutputMessage) {
        this.source = source;
        this.id = id;
        this.status = status;
        this.internalMessage = internalMessage;
        this.userOutputMessage = userOutputMessage;
    }

    ErrorCode(ErrorSource source, int id, HttpStatus status, String internalMessage) {
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
        final var output = Optional.ofNullable(internalMessage).orElse(status.getReasonPhrase());
        return "\"%s\" - ErrorCode.%s".formatted(output, this.name());
    }

    public String getInternalMessage(String... args) {
        final var output = Optional.ofNullable(internalMessage).orElse(status.getReasonPhrase()).formatted((Object[]) args);
        return "\"%s\" - ErrorCode.%s".formatted(output, this.name());
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
