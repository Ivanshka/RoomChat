package by.ivanshka.roomchat.client.chat;

import by.ivanshka.roomchat.client.callback.ExceptionHandlerCallback;
import by.ivanshka.roomchat.client.callback.IncomingPacketCallback;
import by.ivanshka.roomchat.client.exception.AlreadyJoinedRoomException;
import by.ivanshka.roomchat.client.exception.InvalidSessionParameterException;
import by.ivanshka.roomchat.client.network.NetworkClient;
import by.ivanshka.roomchat.common.network.packet.Packet;
import by.ivanshka.roomchat.common.network.packet.impl.MessagePacket;
import by.ivanshka.roomchat.common.network.packet.operation.OperationResultPacket;
import by.ivanshka.roomchat.common.network.packet.operation.impl.ChangeUsernamePacket;
import by.ivanshka.roomchat.common.network.packet.operation.impl.JoinRoomPacket;
import by.ivanshka.roomchat.common.network.packet.operation.impl.LeaveRoomPacket;
import lombok.extern.slf4j.Slf4j;

import java.net.SocketException;
import java.util.function.Consumer;

@Slf4j
public class ChatController implements IncomingPacketCallback, ExceptionHandlerCallback {
    private final NetworkClient client;
    private final ChatSession session;

    private final Consumer<String> eventMessageHandler;

    // todo: add state machine of chat to this class

    public ChatController(String host, int port, Consumer<String> eventMessageHandler) {
        session = new ChatSession();
        client = new NetworkClient(host, port);
        this.eventMessageHandler = eventMessageHandler;
    }

    public void connect() {
        client.connect(this, this);
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
        if (session.isJoinedRoom()) {
            throw new AlreadyJoinedRoomException();
        }

        if (!session.isUsernameSet()) {
            throw new InvalidSessionParameterException("Invalid username");
        }

        client.sendPacket(new JoinRoomPacket(roomId));
    }

    public void leaveRoom() {
        client.sendPacket(new LeaveRoomPacket());
    }

    public void setUsername(String username) {
        ChangeUsernamePacket changeUsernamePacket = new ChangeUsernamePacket(username);
        client.sendPacket(changeUsernamePacket);
    }

    @Override
    public void handleIncomingPacket(Packet packet) {
        switch (packet.getType()) {
            case MESSAGE -> handleMessagePacket((MessagePacket)packet);
            case OPERATION_RESULT -> handleActionResultPacket((OperationResultPacket) packet);
            default -> log.warn("Got unknown packet: " + packet);
        }
    }

    @Override
    public void handleException(Throwable e) {
        if (e instanceof SocketException) {
            log.warn("Server terminated connection unexpectedly. Try to connect again.");
        } else {
            log.error("Exception(", e);
        }
    }

    private void handleMessagePacket(MessagePacket packet) {
        eventMessageHandler.accept(String.format("[%s] %s", packet.getSender(), packet.getMessage()));
    }

    private void handleActionResultPacket(OperationResultPacket packet) {
        switch(packet.getOperation()) {
            case JOIN_ROOM -> handleJoinRoomOperationResult(packet);
            case LEAVE_ROOM -> handleLeaveRoomOperationResult(packet);
            case CHANGE_USERNAME -> handleChangeUsernameOperationResult(packet);
            default -> eventMessageHandler.accept("Got unknown operation package: " + packet);
        }
    }

    private void handleJoinRoomOperationResult(OperationResultPacket packet) {
        if (packet.isSuccess()) {
            String roomId = (String) packet.getArgs().get(0);
            session.setRoomId(roomId);

            eventMessageHandler.accept(String.format("You joined the room \"%s\"", roomId));
        } else {
            eventMessageHandler.accept(String.format("Joining the room failed. Server message: %s",
                    packet.getStatusMessage()));
        }
    }

    private void handleLeaveRoomOperationResult(OperationResultPacket packet) {
        if (packet.isSuccess()) {
            eventMessageHandler.accept("You left the room");
            session.resetRoomId();
        } else {
            eventMessageHandler.accept(String.format("Leaving the room failed. Server message: %s",
                packet.getStatusMessage()));
        }
    }

    private void handleChangeUsernameOperationResult(OperationResultPacket packet) {
        if (packet.isSuccess()) {
            String newUsername = (String) packet.getArgs().get(0);
            session.setUsername(newUsername);
            eventMessageHandler.accept(String.format("You changed username to \"%s\"", newUsername));
        } else {
            eventMessageHandler.accept(String.format("Changing username failed. Server message: %s",
                    packet.getStatusMessage()));
        }
    }
}
