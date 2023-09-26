package by.ivanshka.roomchat.client.command.impl;

import by.ivanshka.roomchat.client.chat.ChatController;
import by.ivanshka.roomchat.client.command.Command;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class LeaveCommand implements Command {
    private static final String COMMAND_STRING = "leave";
    private static final String HELP_STRING = "Leaves current room. After leaving you can join another room.";
    private static final String FULL_HELP_TEXT = """
            Command:
                /LEAVE
            Description:
                Leave the current chat room
            """;
    private final ChatController chatController;

    @Override
    public String getCommandString() {
        return COMMAND_STRING;
    }

    @Override
    public void execute(List<String> args) {
        chatController.leaveRoom();
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
