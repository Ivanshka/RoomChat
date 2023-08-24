package by.ivanshka.roomchat.client.callback;

@FunctionalInterface
public interface ExceptionHandlerCallback {
    void handleException(Throwable e);
}
