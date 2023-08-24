package by.ivanshka.roomchat.common.network.packet.operation.impl;

import by.ivanshka.roomchat.common.network.packet.PacketType;
import by.ivanshka.roomchat.common.network.packet.operation.Operation;
import by.ivanshka.roomchat.common.network.packet.operation.OperationPacket;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChangeUsernamePacket extends OperationPacket {
    private String newUsername;
    @Override
    public PacketType getType() {
        return PacketType.CHANGE_USERNAME_OPERATION;
    }

    @Override
    public void readPacket(ByteBuf buffer) {
        newUsername = readObject(buffer);
    }

    @Override
    public void sendPacket(ByteBuf buffer) {
        writeObject(newUsername, buffer);
    }

    @Override
    public Operation getOperation() {
        return Operation.CHANGE_USERNAME;
    }
}
