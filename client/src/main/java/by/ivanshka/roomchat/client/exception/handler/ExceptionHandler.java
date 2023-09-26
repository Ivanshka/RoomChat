package by.ivanshka.roomchat.client.exception.handler;

@FunctionalInterface
public interface ExceptionHandler {
    void handle(Throwable e);
}
