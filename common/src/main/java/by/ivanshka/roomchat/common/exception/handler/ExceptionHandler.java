package by.ivanshka.roomchat.common.exception.handler;

@FunctionalInterface
public interface ExceptionHandler {
    void handle(Throwable e);
}
