package org.techuni.TechUniInviteSystem.error;

public class MyHttpException extends AbstractHttpException {

    public MyHttpException(ErrorCode statusCode) {
        super(statusCode);
    }

    public MyHttpException(ErrorCode statusCode, String... args) {
        super(statusCode, args);
    }
}
