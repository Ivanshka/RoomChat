package by.ivanshka.roomchat.client.command.impl;

import by.ivanshka.roomchat.client.chat.ChatController;
import by.ivanshka.roomchat.common.command.Command;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ExitCommand implements Command {
    private static final String COMMAND_STRING = "exit";
    private static final String HELP_STRING = "Shut down chat client and exit";
    private static final String FULL_HELP_TEXT = """
            Command:
                /EXIT
            Description:
                Exit from client
            """;
    private final ChatController chatController;

    @Override
    public String getCommandString() {
        return COMMAND_STRING;
    }

    @Override
    public void execute(List<String> args) {
        chatController.disconnectIfConnected();
        log.info("Exiting...");
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
