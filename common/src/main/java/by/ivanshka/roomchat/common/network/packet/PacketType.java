package by.ivanshka.roomchat.common.network.packet;

import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
public enum PacketType {
    SECURITY,
    MESSAGE,
    JOIN_ROOM_OPERATION,
    LEAVE_ROOM_OPERATION,
    CHANGE_USERNAME_OPERATION,
    OPERATION_RESULT;

    private static final Map<Integer, PacketType> ordinal2ValueMap = Arrays.stream(PacketType.values())
            .collect(Collectors.toMap(Enum::ordinal, packetType -> packetType));

    public static PacketType fromTypeNumber(int typeNumber) {
        PacketType value = ordinal2ValueMap.get(typeNumber);

        if (value != null) {
            return value;
        }

        throw new IllegalArgumentException("Packet type has no specified value: " + typeNumber);
    }
}
