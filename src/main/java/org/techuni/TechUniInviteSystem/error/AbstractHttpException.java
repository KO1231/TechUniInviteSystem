package org.techuni.TechUniInviteSystem.error;

import lombok.Getter;
import org.springframework.web.server.ResponseStatusException;

@Getter
public class AbstractHttpException extends ResponseStatusException {

    protected final ErrorCode errorCode;
    protected final String message;

    protected AbstractHttpException(ErrorCode statusCode) {
        super(statusCode.getStatus(), statusCode.getMessage());
        this.errorCode = statusCode;
        this.message = statusCode.getMessage();
    }

    protected AbstractHttpException(ErrorCode statusCode, String... args) {
        super(statusCode.getStatus(), statusCode.getMessage(args));
        this.errorCode = statusCode;
        this.message = statusCode.getMessage(args);
    }
}
