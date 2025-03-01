package org.techuni.TechUniInviteSystem.error;

public class MyHttpException extends AbstractHttpException {

    public MyHttpException(ErrorCode statusCode) {
        super(statusCode);
    }

    public MyHttpException(ErrorCode statusCode, String[] internalArgs, String[] userOutputArgs) {
        super(statusCode, internalArgs, userOutputArgs);
    }

    protected MyHttpException(ErrorCode statusCode, String internalMessage, String[] userOutputArgs) {
        super(statusCode, internalMessage, userOutputArgs);
    }

    public MyHttpException userOutputArgs(String... userOutputArgs) {
        return new MyHttpException(this.errorCode, this.internalMessage, userOutputArgs);
    }
}
