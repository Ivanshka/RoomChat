package by.ivanshka.roomchat.client.exception.impl;

import by.ivanshka.roomchat.client.exception.ApplicationException;

public class DisconnectedException extends ApplicationException {
    private static final String MESSAGE = "Not connected to any server.";
    public DisconnectedException() {
        super(MESSAGE);
    }
}
