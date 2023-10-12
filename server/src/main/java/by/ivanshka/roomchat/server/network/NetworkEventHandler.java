package by.ivanshka.roomchat.server.network;

import by.ivanshka.roomchat.common.exception.handler.ExceptionHandler;
import by.ivanshka.roomchat.common.network.packet.Packet;
import by.ivanshka.roomchat.common.network.packet.impl.MessagePacket;
import by.ivanshka.roomchat.common.network.packet.operation.impl.ChangeUsernamePacket;
import by.ivanshka.roomchat.common.network.packet.operation.impl.JoinRoomPacket;
import by.ivanshka.roomchat.common.network.packet.operation.impl.LeaveRoomPacket;
import by.ivanshka.roomchat.server.chat.Client;
import by.ivanshka.roomchat.server.service.ClientService;
import by.ivanshka.roomchat.server.service.RoomService;
import by.ivanshka.roomchat.server.storage.ClientStorage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Slf4j
@RequiredArgsConstructor
public class NetworkEventHandler extends ChannelInboundHandlerAdapter {
    private final RoomService roomService;
    private final ClientService clientService;
    private final ExceptionHandler exceptionHandler;

    private static final List<Consumer<ChannelHandlerContext>> CONNECTED_EVENT_HANDLERS = new ArrayList<>();
    private static final List<Consumer<ChannelHandlerContext>> DISCONNECTED_EVENT_HANDLERS = new ArrayList<>();

    public static void addConnectedEventHandler(Consumer<ChannelHandlerContext> handler) {
        CONNECTED_EVENT_HANDLERS.add(handler);
    }

    public static void addDisconnectedEventHandler(Consumer<ChannelHandlerContext> handler) {
        DISCONNECTED_EVENT_HANDLERS.add(handler);
    }

    public static void removeConnectedEventHandler(Consumer<ChannelHandlerContext> handler) {
        CONNECTED_EVENT_HANDLERS.remove(handler);
    }

    public static void removeDisconnectedEventHandler(Consumer<ChannelHandlerContext> handler) {
        DISCONNECTED_EVENT_HANDLERS.remove(handler);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        CONNECTED_EVENT_HANDLERS.forEach(handler -> handler.accept(ctx));
        log.info("New client connected: " + ctx.channel().remoteAddress().toString());
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        DISCONNECTED_EVENT_HANDLERS.forEach(handler -> handler.accept(ctx));

        String ip = ctx.channel().remoteAddress().toString();
        log.info("Client disconnected: " + ip);

        super.channelInactive(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        Packet packet = (Packet) msg;
        handlePacket(ctx, packet);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error("Connection was closed cause error shown below.");
        exceptionHandler.handle(cause);
        ctx.close();
    }

    private void handlePacket(ChannelHandlerContext ctx, Packet packet) {
        Client client = clientService.getClientByChannel(ctx.channel());

        switch (packet) {
            case JoinRoomPacket pkt -> roomService.joinRoom(pkt, client);
            case LeaveRoomPacket ignored -> roomService.leaveRoom(client);
            case ChangeUsernamePacket pkt -> clientService.changeUsername(pkt, client);
            case MessagePacket pkt -> roomService.sendToRoomByClient(pkt, client);
            default -> log.warn("Got unknown packet: " + packet);
        }
    }
}
