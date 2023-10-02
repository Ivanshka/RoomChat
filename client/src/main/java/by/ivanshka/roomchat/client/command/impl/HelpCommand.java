package by.ivanshka.roomchat.client.command.impl;

import by.ivanshka.roomchat.common.command.Command;
import by.ivanshka.roomchat.client.exception.impl.CommandExecutionException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static by.ivanshka.roomchat.common.util.StringUtils.isNullOrEmpty;

@Slf4j
@Component
public class HelpCommand implements Command {
    private static final String COMMAND_STRING = "help";
    private static final String SHORT_HELP_STRING = "Show information about how to use specified command. Type /HELP [commandName] to show info.";
    private static final String FULL_HELP_TEXT = """
            Command
                /HELP [commandName]
            Description
                Show detailed information about specified command named [commandName]
            """;
    private final Map<String, Command> commands;

    public HelpCommand(@Autowired List<Command> commands) {
        this.commands = commands
                .stream()
                .collect(
                        Collectors.toMap(
                                command -> command.getCommandString().toLowerCase(),
                                command -> command
                        )
                );
        this.commands.put(COMMAND_STRING, this); // injected list doesn't contain help command cause HelpCommand instance is not created at this moment
    }

    @Override
    public String getCommandString() {
        return COMMAND_STRING;
    }

    @Override
    public void execute(List<String> args) {
        if (args.isEmpty()) {
            showFullHelpPage();
            return;
        }

        if (isNullOrEmpty(args.get(0))) {
            throw new CommandExecutionException("Specify command you want to know about. Type /HELP [commandName] to show info.");
        }

        Command command = commands.get(args.get(0));

        if (command == null) {
            log.info("There is no such command.");
        } else {
            System.out.println(command.getFullHelpText());
        }
    }

    @Override
    public String getShortHelpString() {
        return SHORT_HELP_STRING;
    }

    @Override
    public String getFullHelpText() {
        return FULL_HELP_TEXT;
    }

    private void showFullHelpPage() {
        List<Map.Entry<String, Command>> nameCommandEntries = new ArrayList<>(commands.entrySet());

        nameCommandEntries.sort(Map.Entry.comparingByKey());

        System.out.println("Command           Description");

        for (Map.Entry<String, Command> entry : nameCommandEntries) {
            Command cmd = entry.getValue();
            System.out.format("%15s   %s\n", cmd.getCommandString().toLowerCase(), cmd.getShortHelpString());
        }

        System.out.println("\nYou can know more about concrete command typing /HELP [commandName]");
    }
}
