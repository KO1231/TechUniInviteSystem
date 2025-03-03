package org.techuni.TechUniInviteSystem.error;

public class MyHttpException extends AbstractHttpException {

    public MyHttpException(ErrorCode statusCode) {
        super(statusCode, null);
    }

    public MyHttpException(ErrorCode statusCode, Throwable cause) {
        super(statusCode, cause);
    }

    public MyHttpException(ErrorCode statusCode, Throwable cause, String[] internalArgs, String[] userOutputArgs) {
        super(statusCode, cause, internalArgs, userOutputArgs);
    }

    protected MyHttpException(ErrorCode statusCode, Throwable cause, String internalMessage, String[] userOutputArgs) {
        super(statusCode, cause, internalMessage, userOutputArgs);
    }

    public MyHttpException userOutputArgs(String... userOutputArgs) {
        return new MyHttpException(this.errorCode, this.getCause(), this.internalMessage, userOutputArgs);
    }
}
