package by.ivanshka.roomchat.client.network;

import by.ivanshka.roomchat.client.callback.ExceptionHandlerCallback;
import by.ivanshka.roomchat.client.callback.IncomingPacketCallback;
import by.ivanshka.roomchat.common.network.packet.Packet;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class NetworkEventHandler extends ChannelInboundHandlerAdapter {
    private final IncomingPacketCallback incomingPacketCallback;
    private final ExceptionHandlerCallback exceptionHandlerCallback;

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
        exceptionHandlerCallback.handleException(cause);
        ctx.close();
    }
}
