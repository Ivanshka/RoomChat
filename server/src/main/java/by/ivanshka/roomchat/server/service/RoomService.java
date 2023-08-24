package by.ivanshka.roomchat.server.service;

import by.ivanshka.roomchat.common.network.packet.impl.MessagePacket;
import by.ivanshka.roomchat.common.network.packet.operation.Operation;
import by.ivanshka.roomchat.common.network.packet.operation.OperationResultPacket;
import by.ivanshka.roomchat.common.network.packet.operation.impl.JoinRoomPacket;
import by.ivanshka.roomchat.server.chat.Client;
import by.ivanshka.roomchat.server.chat.room.PublicRoom;
import by.ivanshka.roomchat.server.chat.room.Room;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class RoomService {
    private final Map<String, Room> rooms = new ConcurrentHashMap<>();

    public void broadcastMessage(Client client, MessagePacket packet) {
        client.getRoom().get().broadcast(packet);
    }

    public void removeFromRoomIfJoined(Client client) {
        client.getRoom().ifPresent(r -> {
            MessagePacket notification = new MessagePacket("Server", client.getUsername() + " left the chat room");
            r.broadcast(notification);
            r.getClients().remove(client);
        });
    }

    public void joinRoom(JoinRoomPacket packet, Client client) {
        String roomId = packet.getRoomId();

        Room room = rooms.computeIfAbsent(roomId, (s) -> new PublicRoom(roomId));
        room.getClients().add(client);

        client.setRoom(room);

        OperationResultPacket operationResultPacket = new OperationResultPacket(
                true,
                packet.getOperation(),
                List.of(roomId)
        );
        client.sendPacket(operationResultPacket);

        MessagePacket messagePacket = new MessagePacket("Server", client.getUsername() + " joined the chat room");

        room.broadcast(messagePacket);
    }

    public void leaveRoom(Client client) {
        client.getRoom().ifPresentOrElse(
                r -> {
                    MessagePacket messagePacket = new MessagePacket("Server", client.getUsername() + " left the chat room");
                    r.broadcast(messagePacket);

                    OperationResultPacket resultPacket = new OperationResultPacket(true, Operation.LEAVE_ROOM);
                    client.sendPacket(resultPacket);
                },
                () -> {
                    OperationResultPacket resultPacket = new OperationResultPacket(false, Operation.LEAVE_ROOM, "You are not joined any room");
                    client.sendPacket(resultPacket);
                });
    }
}
