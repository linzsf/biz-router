package org.xltx.bizrouter.exception;

public class NotFoundBizComponentImplException extends RuntimeException{
    public NotFoundBizComponentImplException() {
        super();
    }

    public NotFoundBizComponentImplException(String message) {
        super(message);
    }

    public NotFoundBizComponentImplException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotFoundBizComponentImplException(Throwable cause) {
        super(cause);
    }

    protected NotFoundBizComponentImplException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
