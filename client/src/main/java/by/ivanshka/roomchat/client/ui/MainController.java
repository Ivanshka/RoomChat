package by.ivanshka.roomchat.client.ui;

import by.ivanshka.roomchat.client.chat.ChatController;
import by.ivanshka.roomchat.client.command.Command;
import by.ivanshka.roomchat.client.exception.handler.ExceptionHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

@Slf4j
@Component
public class MainController {
    private final Map<String, Command> commands;
    private final ChatController chatController;
    private final ExceptionHandler exceptionHandler;

    public MainController(@Autowired List<Command> commands,
                          @Autowired ChatController chatController,
                          @Autowired ExceptionHandler exceptionHandler) {
        this.commands = commands
                .stream()
                .collect(
                        Collectors.toMap(
                                command -> command.getCommandString().toLowerCase(),
                                command -> command
                        )
                );
        this.chatController = chatController;
        this.exceptionHandler = exceptionHandler;
    }

    public void start() {

        log.info("Client started! Connect to server, join the room and start chatting!");
        log.info("Type some text to send it or /[commandName] to run command. Type /HELP command to see more.");

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
            chatController.sendMessage(userInput);
        }
    }

    private void handleCommand(String cmd) {
        List<String> commandAndArgs = parseInputToCommandAndArgs(cmd);

        if (commandAndArgs.isEmpty()) {
            return;
        }

        String commandString = commandAndArgs.get(0).toLowerCase();

        Command command = commands.get(commandString);

        if (command != null) {
            commandAndArgs.remove(0); // remove command
            command.execute(commandAndArgs);
        } else {
            log.info("Command not found: " + cmd);
        }
    }

    private List<String> parseInputToCommandAndArgs(String input) {
        char[] chars = input.toCharArray();

        boolean manyWordsArgument = false;
        boolean lastCharIsSpaceOrDoubleQuote = false;

        List<String> myArgs = new ArrayList<>();

        int startArgIndex = 0;

        for (int i = 0; i < chars.length; i++) {
            char ch = chars[i];

            if (ch == '"') {
                manyWordsArgument = !manyWordsArgument;
                if (manyWordsArgument) {
                    startArgIndex = i + 1;
                } else {
                    String arg = input.substring(startArgIndex, i);
                    myArgs.add(arg);
                    startArgIndex = i + 1;
                    lastCharIsSpaceOrDoubleQuote = true;
                }
            } else if (ch == ' ') {
                if (lastCharIsSpaceOrDoubleQuote) {
                    startArgIndex = i + 1;
                    continue;
                }
                if (!manyWordsArgument) {
                    String arg = input.substring(startArgIndex, i);
                    myArgs.add(arg);
                    startArgIndex = i + 1;
                    lastCharIsSpaceOrDoubleQuote = true;
                }
            } else if (i == chars.length - 1) {
                String arg = input.substring(startArgIndex);
                myArgs.add(arg);
            } else {
                lastCharIsSpaceOrDoubleQuote = false;
            }

            if (ch == '\\') {
                i++;
            }
        }

        // parsing some special symbols
        return myArgs.stream()
                .map(arg -> arg.replace("\\\\", "\\")
                        .replace("\\\"", "\"")
                ).collect(Collectors.toList());
    }
}
