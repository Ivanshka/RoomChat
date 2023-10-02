package by.ivanshka.roomchat.server.command.impl;

import by.ivanshka.roomchat.common.command.Command;
import by.ivanshka.roomchat.server.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

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
                Send message as server to specified room or to all rooms. To send message to all rooms use '-a' key before message
            """;
    private final RoomService chatServer;

    @Override
    public String getCommandString() {
        return COMMAND_STRING;
    }

    @Override
    public void execute(List<String> args) {

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
