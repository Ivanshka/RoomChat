package by.ivanshka.roomchat.client.chat;

import by.ivanshka.roomchat.client.exception.impl.AlreadyJoinedRoomException;
import by.ivanshka.roomchat.client.exception.impl.InvalidSessionParameterException;
import by.ivanshka.roomchat.client.network.NetworkClient;
import by.ivanshka.roomchat.common.network.packet.impl.MessagePacket;
import by.ivanshka.roomchat.common.network.packet.operation.impl.ChangeUsernamePacket;
import by.ivanshka.roomchat.common.network.packet.operation.impl.JoinRoomPacket;
import by.ivanshka.roomchat.common.network.packet.operation.impl.LeaveRoomPacket;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChatController {
    private final ChatSession session;
    private final NetworkClient client;

    // todo: add state machine of chat to this class
    @Setter
    @Value("${room-chat.client.host}")
    private String host;
    @Setter
    @Value("${room-chat.client.port}")
    private int port;

    public void connect() {
        client.connect(host, port);
    }

    public void connect(String ip, int port) {
        client.connect(ip, port);
    }

    public void disconnect() {
        client.disconnect();
    }

    public void sendMessage(String message) {
        if (!session.isSetUp()) {
            throw new InvalidSessionParameterException("Username not set or you are not joined room");
        }

        client.sendPacket(new MessagePacket(session.getUsername(), message));
    }

    public void joinRoom(String roomId) {
        if (!session.isUsernameSet()) {
            throw new InvalidSessionParameterException("Set username before running this command.");
        }
        
        if (session.isJoinedRoom()) {
            throw new AlreadyJoinedRoomException();
        }

        log.info("Initiating of joining the room...");

        client.sendPacket(new JoinRoomPacket(roomId));
    }

    public void leaveRoom() {
        client.sendPacket(new LeaveRoomPacket());
    }

    public void setUsername(String username) {
        client.sendPacket(new ChangeUsernamePacket(username));
    }

    public void disconnectIfConnected() {
        if (client.isConnected()) {
            disconnect();
        }
    }
}
