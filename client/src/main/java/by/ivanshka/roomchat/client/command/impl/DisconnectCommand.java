package by.ivanshka.roomchat.client.command.impl;

import by.ivanshka.roomchat.client.chat.ChatController;
import by.ivanshka.roomchat.client.command.Command;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DisconnectCommand implements Command {
    private static final String COMMAND_STRING = "disconnect";
    private static final String HELP_STRING = "Disconnect from current chat server";
    private static final String FULL_HELP_TEXT = """
            Command:
                /DISCONNECT
            Description:
                Disconnect from server. After disconnecting you can connect to another server.
            """;
    private final ChatController chatController;

    @Override
    public String getCommandString() {
        return COMMAND_STRING;
    }

    @Override
    public void execute(List<String> args) {
        chatController.disconnect();
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
