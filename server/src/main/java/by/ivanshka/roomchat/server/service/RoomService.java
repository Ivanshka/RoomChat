package by.ivanshka.roomchat.server.service;

import by.ivanshka.roomchat.common.network.packet.impl.MessagePacket;
import by.ivanshka.roomchat.common.network.packet.operation.Operation;
import by.ivanshka.roomchat.common.network.packet.operation.OperationResultPacket;
import by.ivanshka.roomchat.common.network.packet.operation.impl.JoinRoomPacket;
import by.ivanshka.roomchat.server.chat.Client;
import by.ivanshka.roomchat.server.chat.room.Room;
import by.ivanshka.roomchat.server.storage.RoomStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.InvalidParameterException;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class RoomService {
    private final RoomStorage roomStorage;

    @Value("${room-chat.server.public-title}")
    private String serverTitle;

    public void sendToRoomByClient(MessagePacket packet, Client client) {
        client.getRoom()
                .orElseThrow()
                .broadcast(packet);
    }

    public void removeFromRoomIfJoined(Client client) {
        client.getRoom().ifPresent(r -> {
            MessagePacket notification = new MessagePacket(serverTitle, client.getUsername() + " left the chat room");
            r.broadcast(notification);
            r.getClients().remove(client);
        });
    }

    public void joinRoom(JoinRoomPacket packet, Client client) {
        String roomId = packet.getRoomId();

        Room room = roomStorage.save(roomId);
        room.getClients().add(client);

        client.setRoom(room);

        OperationResultPacket operationResultPacket = new OperationResultPacket(
                true,
                packet.getOperation(),
                List.of(roomId)
        );
        client.sendPacket(operationResultPacket);

        MessagePacket messagePacket = new MessagePacket(serverTitle, client.getUsername() + " joined the chat room");

        room.broadcast(messagePacket);
    }

    public void leaveRoom(Client client) {
        client.getRoom().ifPresentOrElse(
                r -> {
                    MessagePacket messagePacket = new MessagePacket(serverTitle, client.getUsername() + " left the chat room");
                    r.broadcast(messagePacket);

                    OperationResultPacket resultPacket = new OperationResultPacket(true, Operation.LEAVE_ROOM);
                    client.sendPacket(resultPacket);
                },
                () -> {
                    OperationResultPacket resultPacket = new OperationResultPacket(false, Operation.LEAVE_ROOM, "You are not joined any room");
                    client.sendPacket(resultPacket);
                });
    }

    public void broadcastToAllRooms(String message) {
        MessagePacket messagePacket = new MessagePacket(serverTitle, message);
        roomStorage.getAll()
                .forEach(room -> room.broadcast(messagePacket));
    }

    public void sendToRoom(String roomName, String message) {
        Room room = roomStorage.getByName(roomName)
                .orElseThrow(() -> new InvalidParameterException("Room is not found"));

        MessagePacket messagePacket = new MessagePacket(serverTitle, message);
        room.broadcast(messagePacket);
    }

    public int countRooms() {
        return roomStorage.count();
    }
}
