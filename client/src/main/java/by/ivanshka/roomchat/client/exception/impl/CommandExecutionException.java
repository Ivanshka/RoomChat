package by.ivanshka.roomchat.client.exception.impl;

import by.ivanshka.roomchat.client.exception.ApplicationException;

public class CommandExecutionException extends ApplicationException {
    public CommandExecutionException() {
    }

    public CommandExecutionException(String message) {
        super(message);
    }

    public CommandExecutionException(String message, Throwable cause) {
        super(message, cause);
    }

    public CommandExecutionException(Throwable cause) {
        super(cause);
    }

    public CommandExecutionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
