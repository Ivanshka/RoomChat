package by.ivanshka.roomchat.client.callback.impl;

import by.ivanshka.roomchat.client.callback.IncomingPacketCallback;
import by.ivanshka.roomchat.client.chat.ChatSession;
import by.ivanshka.roomchat.common.network.packet.Packet;
import by.ivanshka.roomchat.common.network.packet.impl.MessagePacket;
import by.ivanshka.roomchat.common.network.packet.operation.OperationResultPacket;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Slf4j
@Component
@RequiredArgsConstructor
public class IncomingPacketCallbackImpl implements IncomingPacketCallback {
    private final Consumer<String> eventMessageHandler;
    private final ChatSession session;

    @Override
    public void handleIncomingPacket(Packet packet) {
        switch (packet.getType()) {
            case MESSAGE -> handleMessagePacket((MessagePacket) packet);
            case OPERATION_RESULT -> handleActionResultPacket((OperationResultPacket) packet);
            default -> log.warn("Got unknown packet: " + packet);
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
