package by.ivanshka.roomchat.server;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Main {
    @SneakyThrows
    public static void main(String[] args) {

        ChatServer.run();
    }
}
