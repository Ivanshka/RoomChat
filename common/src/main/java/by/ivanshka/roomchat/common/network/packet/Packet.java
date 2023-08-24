package by.ivanshka.roomchat.common.network.packet;

import by.ivanshka.roomchat.common.util.SerializationUtils;
import io.netty.buffer.ByteBuf;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class Packet {

    public static Packet readMetaDataAndPacket(ByteBuf buffer) {
        int packetId = buffer.readInt();

        Packet packet = PacketFactory.getPacketInstance(packetId);

        try {
            packet.readPacket(buffer);
            return packet;
        } catch (Exception e) {
            log.error("Packet reading failed", e);
            throw e;
        }
    }

    public static void sendMetaDataAndPacket(Packet packet, ByteBuf buffer) {
        buffer.writeInt(packet.getType().ordinal());

        try {
            packet.sendPacket(buffer);
        } catch (Exception e) {
            log.error("Packet writing failed", e);
            throw e;
        }
    }

    public abstract PacketType getType();

    public abstract void readPacket(ByteBuf buffer);
    public abstract void sendPacket(ByteBuf buffer);

    protected void writeObject(Object o, ByteBuf buffer) {
        byte[] bytes = SerializationUtils.serialize(o);
        buffer.writeInt(bytes.length);
        buffer.writeBytes(bytes);
    }

    protected <T> T readObject(ByteBuf buffer) {
        int length = buffer.readInt();
        byte[] dataBuffer = new byte[length];
        buffer.readBytes(dataBuffer);
        return SerializationUtils.deserialize(dataBuffer);
    }
}
