package by.ivanshka.roomchat.common.network.packet.operation;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public enum Operation {
    JOIN_ROOM,
    LEAVE_ROOM,
    CHANGE_USERNAME;

    private static final Map<Integer, Operation> ordinal2ValueMap = Arrays.stream(Operation.values())
            .collect(Collectors.toMap(Enum::ordinal, packetType -> packetType));

    public static Operation fromTypeNumber(int typeNumber) {
        Operation value = ordinal2ValueMap.get(typeNumber);

        if (value != null) {
            return value;
        }

        throw new IllegalArgumentException("Packet type has no specified value: " + typeNumber);
    }
}
