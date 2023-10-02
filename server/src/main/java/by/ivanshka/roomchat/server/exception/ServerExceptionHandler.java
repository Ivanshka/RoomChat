package by.ivanshka.roomchat.server.exception;

import by.ivanshka.roomchat.common.exception.handler.AbstractExceptionHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ServerExceptionHandler extends AbstractExceptionHandler {
    @Override
    public void handle(Throwable e) {
        switch (e) {
            default -> log.error("Unhandled exception!", e);
        }
    }
}
