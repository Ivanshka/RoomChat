package by.ivanshka.roomchat.client.exception.impl;

import by.ivanshka.roomchat.client.exception.ApplicationException;

public class InvalidSessionParameterException extends ApplicationException {
    public InvalidSessionParameterException(String message) {
        super(message);
    }
}
