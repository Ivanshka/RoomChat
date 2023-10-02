package by.ivanshka.roomchat.server.command.impl;

import by.ivanshka.roomchat.common.command.Command;
import by.ivanshka.roomchat.server.ChatServer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class StopCommand implements Command {
    private static final String COMMAND_STRING = "stop";
    private static final String HELP_STRING = "Shut down chat server and exit";
    private static final String FULL_HELP_TEXT = """
            Command:
                /STOP
            Description:
                Stop the chat server and exit
            """;
    private final ChatServer chatServer;

    @Override
    public String getCommandString() {
        return COMMAND_STRING;
    }

    @Override
    public void execute(List<String> args) {
        chatServer.stop();
        System.exit(0);
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
