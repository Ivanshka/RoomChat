package by.ivanshka.roomchat.server.network;

import by.ivanshka.roomchat.common.network.packet.Packet;
import by.ivanshka.roomchat.common.network.packet.impl.MessagePacket;
import by.ivanshka.roomchat.common.network.packet.operation.impl.ChangeUsernamePacket;
import by.ivanshka.roomchat.common.network.packet.operation.impl.JoinRoomPacket;
import by.ivanshka.roomchat.server.chat.Client;
import by.ivanshka.roomchat.server.service.ClientService;
import by.ivanshka.roomchat.server.service.RoomService;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.net.SocketException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@RequiredArgsConstructor
public class NetworkEventHandler extends ChannelInboundHandlerAdapter {
    private static final Map<Channel, Client> clients = new ConcurrentHashMap<>();
    private final RoomService roomService;
    private final ClientService clientService;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        Client newClient = new Client(channel);
        clients.put(channel, newClient);
        log.info("New client was connected: " + channel.remoteAddress().toString());
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        Client client = clients.get(channel);

        clients.remove(channel);
        roomService.removeFromRoomIfJoined(client);

        String ip = client.getChannel().remoteAddress().toString();
        log.info("Client was disconnected: " + ip);

        super.channelInactive(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        Packet packet = (Packet) msg;

        Channel channel = ctx.channel();
        Client client = clients.get(channel);

        switch (packet.getType()) {
            case JOIN_ROOM_OPERATION -> roomService.joinRoom((JoinRoomPacket) packet, client);
            case LEAVE_ROOM_OPERATION -> roomService.leaveRoom(client);
            case CHANGE_USERNAME_OPERATION -> clientService.changeUsername((ChangeUsernamePacket) packet, client);
            case MESSAGE -> roomService.broadcastMessage(client, (MessagePacket) packet);
            default -> log.warn("Got unknown packet: " + packet);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        if (cause instanceof SocketException) {
            log.warn("Client terminated connection unexpectedly: " + cause.getMessage());
        } else {
            log.error("Connection was closed cause unexpectedly exception. Stacktrace is shown below.", cause);
        }

        ctx.close();
    }
}
