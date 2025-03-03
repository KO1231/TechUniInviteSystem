package org.techuni.TechUniInviteSystem.error;

import lombok.Getter;
import org.springframework.web.server.ResponseStatusException;

@Getter
public abstract class AbstractHttpException extends ResponseStatusException {

    protected final ErrorCode errorCode;
    protected final String internalMessage;
    protected final String userOutputMessage;

    private AbstractHttpException(ErrorCode statusCode, Throwable cause, String message, String userOutputMessage) {
        super(statusCode.getStatus(), message, cause);
        this.errorCode = statusCode;
        this.internalMessage = message;
        this.userOutputMessage = userOutputMessage;
    }

    protected AbstractHttpException(ErrorCode statusCode, Throwable cause) {
        this(statusCode, cause, statusCode.getInternalMessage(), statusCode.getUserOutputMessage());
    }

    protected AbstractHttpException(ErrorCode statusCode, Throwable cause, String internalMessage, String[] userOutputArgs) {
        this(statusCode, cause, internalMessage, statusCode.getUserOutputMessage().formatted((Object[]) userOutputArgs));
    }

    protected AbstractHttpException(ErrorCode statusCode, Throwable cause, String[] internalArgs, String[] userOutputArgs) {
        this(statusCode, //
                cause, //
                statusCode.getInternalMessage(internalArgs), //
                statusCode.getUserOutputMessage().formatted((Object[]) userOutputArgs));
    }
}
