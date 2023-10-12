package by.ivanshka.roomchat.server.command.impl;

import by.ivanshka.roomchat.common.command.Command;
import by.ivanshka.roomchat.common.exception.impl.CommandExecutionException;
import by.ivanshka.roomchat.server.service.RoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.security.InvalidParameterException;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class SayCommand implements Command {
    private static final String COMMAND_STRING = "say";
    private static final String HELP_STRING = "Send message as server to specified room or to all rooms";
    private static final String FULL_HELP_TEXT = """
            Command:
                /SAY [roomName] yourMessage
                /SAY -a yourMessage
            Description:
                Send message as server to specified room or to all rooms. To send message to ALL rooms use '-a' key before message.
            """;
    private final RoomService roomService;

    @Override
    public String getCommandString() {
        return COMMAND_STRING;
    }

    @Override
    public void execute(List<String> args) {
        if (isMessageForAllRooms(args)) {
            args.remove(0);
            String message = String.join(" ", args);

            roomService.broadcastToAllRooms(message);

            log.info(String.format("Message sent to ALL rooms: \"%s\"", message));
        } else {
            String roomName = args.get(0);

            args.remove(0);
            String message = String.join(" ", args);

            try {
                roomService.sendToRoom(roomName, message);
                log.info(String.format("Message sent to room \"%s\": \"%s\"", roomName, message));

            } catch (InvalidParameterException e) {
                throw new CommandExecutionException("Room not found", e);
            }
        }
    }

    private static boolean isMessageForAllRooms(List<String> args) {
        return args.get(0).equalsIgnoreCase("-a");
    }

    @Override
    public String getShortHelpString() {
        return HELP_STRING;
    }

    @Override
    public String getFullHelpText() {
        return FULL_HELP_TEXT;
    }
}
