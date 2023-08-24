package by.ivanshka.roomchat.common.network.packet.operation;

import by.ivanshka.roomchat.common.network.packet.Packet;
import by.ivanshka.roomchat.common.network.packet.PacketType;
import io.netty.buffer.ByteBuf;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class OperationResultPacket extends Packet {
    private boolean isSuccess;
    private Operation operation;
    private String statusMessage;
    private List<Object> args;

    public OperationResultPacket(boolean isSuccess, Operation operation) {
        this.isSuccess = isSuccess;
        this.operation = operation;
    }

    public OperationResultPacket(boolean isSuccess, Operation operation, List<Object> args) {
        this.isSuccess = isSuccess;
        this.operation = operation;
        this.args = args;
    }

    public OperationResultPacket(boolean isSuccess, Operation operation, String statusMessage) {
        this.isSuccess = isSuccess;
        this.operation = operation;
        this.statusMessage = statusMessage;
    }

    @Override
    public PacketType getType() {
        return PacketType.OPERATION_RESULT;
    }

    @Override
    public void readPacket(ByteBuf buffer) {
        isSuccess = buffer.readBoolean();
        operation = Operation.fromTypeNumber(buffer.readInt());
        statusMessage = readObject(buffer);
        args = readObject(buffer);
    }

    @Override
    public void sendPacket(ByteBuf buffer) {
        buffer.writeBoolean(isSuccess);
        buffer.writeInt(operation.ordinal());
        writeObject(statusMessage, buffer);
        writeObject(args, buffer);
    }
}
