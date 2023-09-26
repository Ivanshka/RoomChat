package by.ivanshka.roomchat.client.exception.handler.impl;

import by.ivanshka.roomchat.client.exception.impl.AlreadyJoinedRoomException;
import by.ivanshka.roomchat.client.exception.impl.CommandExecutionException;
import by.ivanshka.roomchat.client.exception.impl.DisconnectedException;
import by.ivanshka.roomchat.client.exception.impl.InvalidSessionParameterException;
import by.ivanshka.roomchat.client.exception.handler.ExceptionHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.net.SocketException;

@Slf4j
@Component
public class ChatRoomExceptionHandler implements ExceptionHandler {
    @Override
    public void handle(Throwable e) {
        switch (e) {
            case InvalidSessionParameterException ignored -> printExceptionMessage(e);

            case DisconnectedException ignored -> printExceptionMessage(e);

            case AlreadyJoinedRoomException ignored -> printExceptionMessage(e);

            case CommandExecutionException ignored -> printExceptionMessage(e);

            case SocketException ignored -> log.warn("Server terminated connection unexpectedly. Try to connect again.");

            default -> log.error("Unhandled exception!", e);
        }
    }

    private static void printExceptionMessage(Throwable e) {
        log.warn("Error. " + e.getMessage());
    }
}
