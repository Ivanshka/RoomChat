package by.ivanshka.roomchat.client.exception.impl;

import by.ivanshka.roomchat.client.exception.ApplicationException;

public class AlreadyJoinedRoomException extends ApplicationException {
    private static final String MESSAGE = "You have to leave current room before joining new.";
    public AlreadyJoinedRoomException() {
        super(MESSAGE);
    }
}
