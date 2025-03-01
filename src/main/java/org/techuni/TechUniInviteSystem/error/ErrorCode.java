package org.techuni.TechUniInviteSystem.error;

import static org.springframework.util.ObjectUtils.isEmpty;

import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    /**
     * LOGIN
     **/
    LOGIN_FAILED(ErrorSource.LOGIN, 1, HttpStatus.UNAUTHORIZED), //
    LOGIN_USER_DENIED(ErrorSource.LOGIN, 2, HttpStatus.UNAUTHORIZED, "Denied user tried to login. (User: %s)"), //
    LOGIN_UNEXCEPTED_ERROR(ErrorSource.LOGIN, 3, HttpStatus.UNAUTHORIZED, "Unexpected error occurred."), //
    LOGIN_REQUEST_VALIDATION_ERROR(ErrorSource.LOGIN, 4, HttpStatus.UNAUTHORIZED), //

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
    private final String message;

    public int getId() {
        return source.getId() * SOURCE_MARGIN + id;
    }

    public String getMessage(String... args) {
        if (isEmpty(args)) {
            return getMessage();
        }
        return getMessage().formatted((Object[]) args);
    }

    ErrorCode(ErrorSource source, int id, HttpStatus status) {
        this(source, id, status, null);
    }

    public MyHttpException exception() {
        return new MyHttpException(this);
    }

    public MyHttpException exception(String... args) {
        return new MyHttpException(this, args);
    }

    public String getMessage() {
        final var output = Optional.ofNullable(message).orElse(status.getReasonPhrase());
        return "\"%s\" - ErrorCode.%s".formatted(output, this.name());
    }

    @Getter
    @AllArgsConstructor
    private enum ErrorSource {
        OTHER(0), //
        LOGIN(1), //
        CONTACT_FORM(2), //
        VALIDATION(3), //
        MAIL(4);

        private final int id;
    }
}
