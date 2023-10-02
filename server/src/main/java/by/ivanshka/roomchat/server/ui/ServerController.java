package by.ivanshka.roomchat.server.ui;

import by.ivanshka.roomchat.common.command.Command;
import by.ivanshka.roomchat.common.exception.handler.ExceptionHandler;
import by.ivanshka.roomchat.common.util.CommandStringParser;
import by.ivanshka.roomchat.server.ChatServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

@Slf4j
@Component
public class ServerController {
    private final ChatServer server;
    private final Map<String, Command> commands;
    private final ExceptionHandler exceptionHandler;

    public ServerController(@Autowired ChatServer server,
                            @Autowired List<Command> commands,
                            @Autowired ExceptionHandler exceptionHandler) {
        this.server = server;
        this.commands = commands
                .stream()
                .collect(
                        Collectors.toMap(
                                command -> command.getCommandString().toLowerCase(),
                                command -> command
                        )
                );
        this.exceptionHandler = exceptionHandler;
    }

    public void start() {
        server.start();

        log.info("Chat server started!");
        log.info("Type /[commandName] to run command. Type /HELP command to see more.");

        Scanner scanner = new Scanner(System.in);

        while (true) {
            try {
                String input = scanner.nextLine();
                handleUserInput(input);
            } catch (Exception e) {
                exceptionHandler.handle(e);
            }
        }
    }

    private void handleUserInput(String userInput) {
        if (userInput.startsWith("/")) {
            String command = userInput.substring(1);
            handleCommand(command);
        } else {
            log.warn("Command should start with '/' symbol");
        }
    }

    private void handleCommand(String cmd) {
        List<String> commandAndArgs = CommandStringParser.parseInputToCommandAndArgs(cmd);

        if (commandAndArgs.isEmpty()) {
            return;
        }

        String commandString = commandAndArgs.get(0).toLowerCase();

        Command command = commands.get(commandString);

        if (command != null) {
            commandAndArgs.remove(0); // remove command
            command.execute(commandAndArgs);
        } else {
            log.info("Command not found: " + commandString);
        }
    }
}
