package by.ivanshka.roomchat.server.exception;

import by.ivanshka.roomchat.common.exception.handler.AbstractExceptionHandler;
import by.ivanshka.roomchat.common.exception.impl.CommandExecutionException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.net.SocketException;

@Slf4j
@Component
public class ServerExceptionHandler extends AbstractExceptionHandler {
    @Override
    public void handle(Throwable e) {
        switch (e) {
            case CommandExecutionException ignored -> printExceptionMessage(e);

            case SocketException ex -> log.warn("Client terminated connection unexpectedly: " + ex.getMessage());

            default -> log.error("Unhandled exception!", e);
        }
    }
}
