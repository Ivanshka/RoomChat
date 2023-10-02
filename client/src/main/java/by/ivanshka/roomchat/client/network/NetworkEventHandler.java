package by.ivanshka.roomchat.client.network;

import by.ivanshka.roomchat.client.callback.IncomingPacketCallback;
import by.ivanshka.roomchat.common.exception.handler.ExceptionHandler;
import by.ivanshka.roomchat.common.network.packet.Packet;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class NetworkEventHandler extends ChannelInboundHandlerAdapter {
    private final IncomingPacketCallback incomingPacketCallback;
    private final ExceptionHandler exceptionHandler;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("Connected. You can join the room and start chatting.");
        log.debug("Connected to " + ctx.channel().remoteAddress().toString());
        super.channelInactive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("Connection was closed");
        super.channelInactive(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object pkt) {
        incomingPacketCallback.handleIncomingPacket((Packet) pkt);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        exceptionHandler.handle(cause);
        ctx.close();
    }
}
