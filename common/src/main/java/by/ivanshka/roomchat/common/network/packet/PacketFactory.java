package by.ivanshka.roomchat.common.network.packet;

import by.ivanshka.roomchat.common.network.packet.operation.OperationResultPacket;
import by.ivanshka.roomchat.common.network.packet.impl.MessagePacket;
import by.ivanshka.roomchat.common.network.packet.operation.impl.ChangeUsernamePacket;
import by.ivanshka.roomchat.common.network.packet.operation.impl.JoinRoomPacket;
import by.ivanshka.roomchat.common.network.packet.operation.impl.LeaveRoomPacket;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

@Slf4j
public class PacketFactory {
    private static final Map<PacketType, Class<? extends Packet>> type2PacketMap = Map.ofEntries(
            Map.entry(PacketType.MESSAGE, MessagePacket.class),
            Map.entry(PacketType.JOIN_ROOM_OPERATION, JoinRoomPacket.class),
            Map.entry(PacketType.LEAVE_ROOM_OPERATION, LeaveRoomPacket.class),
            Map.entry(PacketType.CHANGE_USERNAME_OPERATION, ChangeUsernamePacket.class),
            Map.entry(PacketType.OPERATION_RESULT, OperationResultPacket.class)
        );

    public static Packet getPacketInstance(int packetId) {
        PacketType packetType = PacketType.fromTypeNumber(packetId);

        Class<? extends Packet> packetClass = type2PacketMap.get(packetType);
        if (packetClass == null) {
            throw new IllegalArgumentException("Invalid packet id: " + packetId);
        }

        return createPacketSafely(packetClass);
    }

    private static Packet createPacketSafely(Class<? extends Packet> packetClass) {
        try {
            return packetClass.getConstructor().newInstance();

        } catch (NoSuchMethodException e) {
            log.error("Packet class [" + packetClass.getTypeName() + "] must have public default constructor", e);
            throw new RuntimeException(e);

        } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
            log.error("Failed to create a packet instance", e);
            throw new RuntimeException(e);
        }
    }
}
