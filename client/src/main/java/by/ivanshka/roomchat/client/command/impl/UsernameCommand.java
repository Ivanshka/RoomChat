package by.ivanshka.roomchat.client.command.impl;

import by.ivanshka.roomchat.client.chat.ChatController;
import by.ivanshka.roomchat.client.command.Command;
import by.ivanshka.roomchat.client.exception.impl.CommandExecutionException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

import static by.ivanshka.roomchat.common.util.StringUtils.isNullOrEmpty;

@Component
@RequiredArgsConstructor
public class UsernameCommand implements Command {
    private static final String COMMAND_STRING = "username";
    private static final String HELP_STRING = "Set new username";
    private static final String FULL_HELP_TEXT = """
            Command:
                /USERNAME
            Description:
                Set username required for chatting
            """;
    private final ChatController chatController;

    @Override
    public String getCommandString() {
        return COMMAND_STRING;
    }

    @Override
    public void execute(List<String> args) {
        if (args.isEmpty() || isNullOrEmpty(args.get(0))) {
            throw new CommandExecutionException("Username can't be empty.");
        }

        chatController.setUsername(args.get(0));
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
