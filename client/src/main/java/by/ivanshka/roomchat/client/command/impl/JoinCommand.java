package by.ivanshka.roomchat.client.command.impl;

import by.ivanshka.roomchat.client.chat.ChatController;
import by.ivanshka.roomchat.common.command.Command;
import by.ivanshka.roomchat.common.exception.impl.CommandExecutionException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

import static by.ivanshka.roomchat.common.util.StringUtils.isNullOrEmpty;

@Slf4j
@Component
@RequiredArgsConstructor
public class JoinCommand implements Command {
    private static final String COMMAND_STRING = "join";
    private static final String HELP_STRING = "Joins the specified room by its name";
    private static final String FULL_HELP_TEXT = """
            Command:
                /JOIN [roomName]
            Description:
                Try to join the room named [roomName]
            """;
    private final ChatController chatController;

    @Override
    public String getCommandString() {
        return COMMAND_STRING;
    }

    @Override
    public void execute(List<String> args) {
        if (args.isEmpty() || isNullOrEmpty(args.get(0))) {
            throw new CommandExecutionException("Room name can't be empty.");
        }

        String roomName = args.get(0);

        chatController.joinRoom(roomName);
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
