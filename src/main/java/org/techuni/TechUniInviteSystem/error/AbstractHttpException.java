package org.techuni.TechUniInviteSystem.error;

import lombok.Getter;
import org.springframework.web.server.ResponseStatusException;

@Getter
public abstract class AbstractHttpException extends ResponseStatusException {

    protected final ErrorCode errorCode;
    protected final String internalMessage;
    protected final String userOutputMessage;

    private AbstractHttpException(ErrorCode statusCode, String message, String userOutputMessage) {
        super(statusCode.getStatus(), message);
        this.errorCode = statusCode;
        this.internalMessage = message;
        this.userOutputMessage = userOutputMessage;
    }

    protected AbstractHttpException(ErrorCode statusCode) {
        this(statusCode, statusCode.getInternalMessage(), statusCode.getUserOutputMessage());
    }

    protected AbstractHttpException(ErrorCode statusCode, String internalMessage, String[] userOutputArgs) {
        this(statusCode, internalMessage, statusCode.getUserOutputMessage().formatted((Object[]) userOutputArgs));
    }

    protected AbstractHttpException(ErrorCode statusCode, String[] internalArgs, String[] userOutputArgs) {
        this(statusCode, //
                statusCode.getInternalMessage(internalArgs), //
                statusCode.getUserOutputMessage().formatted((Object[]) userOutputArgs));
    }
}
