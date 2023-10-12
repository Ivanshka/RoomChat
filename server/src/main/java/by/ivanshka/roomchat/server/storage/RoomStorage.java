package by.ivanshka.roomchat.server.storage;

import by.ivanshka.roomchat.server.chat.room.PublicRoom;
import by.ivanshka.roomchat.server.chat.room.Room;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RoomStorage {
    private final Map<String, Room> rooms = new ConcurrentHashMap<>();

    public Collection<Room> getAll() {
        return rooms.values();
    }

    public Optional<Room> getByName(String name) {
        return Optional.of(rooms.get(name));
    }

    public Room save(String roomName) {
        return rooms.computeIfAbsent(roomName, (s) -> new PublicRoom(roomName));
    }

    public void remove(String roomName) {
        rooms.remove(roomName);
    }

    public int count() {
        return rooms.size();
    }
}
