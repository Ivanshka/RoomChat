package by.ivanshka.roomchat.common.network.packet.operation.impl;

import by.ivanshka.roomchat.common.network.packet.PacketType;
import by.ivanshka.roomchat.common.network.packet.operation.Operation;
import by.ivanshka.roomchat.common.network.packet.operation.OperationPacket;
import io.netty.buffer.ByteBuf;

public class LeaveRoomPacket extends OperationPacket {
    @Override
    public PacketType getType() {
        return PacketType.LEAVE_ROOM_OPERATION;
    }

    @Override
    public void readPacket(ByteBuf buffer) {

    }

    @Override
    public void sendPacket(ByteBuf buffer) {

    }

    @Override
    public Operation getOperation() {
        return Operation.LEAVE_ROOM;
    }
}
