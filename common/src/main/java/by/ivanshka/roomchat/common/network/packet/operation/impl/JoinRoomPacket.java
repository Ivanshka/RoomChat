package by.ivanshka.roomchat.common.network.packet.operation.impl;

import by.ivanshka.roomchat.common.network.packet.PacketType;
import by.ivanshka.roomchat.common.network.packet.operation.Operation;
import by.ivanshka.roomchat.common.network.packet.operation.OperationPacket;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class JoinRoomPacket extends OperationPacket {
    private String roomId;

    @Override
    public PacketType getType() {
        return PacketType.JOIN_ROOM_OPERATION;
    }

    @Override
    public Operation getOperation() {
        return Operation.JOIN_ROOM;
    }

    @Override
    public void readPacket(ByteBuf buffer) {
        roomId = readObject(buffer);
    }

    @Override
    public void sendPacket(ByteBuf buffer) {
        writeObject(roomId, buffer);
    }
}
