package by.ivanshka.roomchat.common.network.decoder;

import by.ivanshka.roomchat.common.network.packet.Packet;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

import java.util.List;

public class PacketDecoder extends ReplayingDecoder<Packet> {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        Packet packet = Packet.readMetaDataAndPacket(in);
        out.add(packet);
    }

}
