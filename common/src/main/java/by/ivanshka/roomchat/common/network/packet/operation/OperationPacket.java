package by.ivanshka.roomchat.common.network.packet.operation;

import by.ivanshka.roomchat.common.network.packet.Packet;

public abstract class OperationPacket extends Packet {
    public abstract Operation getOperation();
}
