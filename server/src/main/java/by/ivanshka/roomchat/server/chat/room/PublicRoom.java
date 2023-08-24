package by.ivanshka.roomchat.server.chat.room;


import java.util.concurrent.CopyOnWriteArrayList;

public class PublicRoom extends Room {
    public PublicRoom(String id) {
        this.id = id;
        this.clients = new CopyOnWriteArrayList<>();
    }
}
