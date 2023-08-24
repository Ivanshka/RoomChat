package by.ivanshka.roomchat.common.network.packet.impl;

import by.ivanshka.roomchat.common.network.packet.Packet;
import by.ivanshka.roomchat.common.network.packet.PacketType;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MessagePacket extends Packet {
    private String sender;
    private String message;

    @Override
    public PacketType getType() {
        return PacketType.MESSAGE;
    }

    @Override
    public void readPacket(ByteBuf buffer) {
        sender = readObject(buffer);
        message = readObject(buffer);
    }

    @Override
    public void sendPacket(ByteBuf buffer) {
        writeObject(sender, buffer);
        writeObject(message, buffer);
    }
}
