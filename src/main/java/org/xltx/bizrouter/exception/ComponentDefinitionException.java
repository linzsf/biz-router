package org.xltx.bizrouter.exception;

public class ComponentDefinitionException extends RuntimeException{

    public ComponentDefinitionException() {
        super();
    }

    public ComponentDefinitionException(String message) {
        super(message);
    }

    public ComponentDefinitionException(String message, Throwable cause) {
        super(message, cause);
    }

    public ComponentDefinitionException(Throwable cause) {
        super(cause);
    }

    protected ComponentDefinitionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
