package by.ivanshka.roomchat.client.command.impl;

import by.ivanshka.roomchat.client.chat.ChatController;
import by.ivanshka.roomchat.common.command.Command;
import by.ivanshka.roomchat.client.exception.impl.CommandExecutionException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.regex.Pattern;

import static by.ivanshka.roomchat.common.util.StringUtils.isNullOrEmpty;

@Slf4j
@Component
@RequiredArgsConstructor
public class ConnectCommand implements Command {
    private static final String COMMAND_STRING = "connect";
    private static final String HELP_STRING = "Connect to chat server";

    private static final Pattern IP_ADDRESS_PATTERN = Pattern.compile(
            "^(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])$");
    private static final String FULL_HELP_TEXT = """
            Command:
                /CONNECT
                /CONNECT [ipAddress]:[port]
                /CONNECT [ipAddress] [port]
            Description:
                Connect to server with specified IP and port
            """;
    private final ChatController chatController;

    @Override
    public String getCommandString() {
        return COMMAND_STRING;
    }

    @Override
    public void execute(List<String> args) {
        if (args.isEmpty()) {
            chatController.connect();
            log.info("Default settings will be used to connect");
            return;
        }

        String ip;
        String rawPort;

        if (args.size() == 1) {
            if (isNullOrEmpty(args.get(0))) {
                throw new CommandExecutionException("Requires both of IP and port.");
            }

            String[] ipAndPort = args.get(0).split(":", 2);

            if (ipAndPort.length != 2) {
                throw new CommandExecutionException("IP address and port expected in format [ip:port].");
            }

            ip = ipAndPort[0];
            rawPort = ipAndPort[1];
        } else {
            ip = args.get(0);
            rawPort = args.get(1);
        }

        if (!validateIp(ip)) {
            throw new CommandExecutionException("Incorrect IP address or port.");
        }

        int port = parsePort(rawPort);

        chatController.connect(ip, port);
    }

    @Override
    public String getShortHelpString() {
        return HELP_STRING;
    }

    @Override
    public String getFullHelpText() {
        return FULL_HELP_TEXT;
    }

    private boolean validateIp(String ip) {
        return IP_ADDRESS_PATTERN.matcher(ip).matches();
    }

    private int parsePort(String port) {
        try {
            int p = Integer.parseInt(port);
            if (p < 0 || p > 65535) {
                throw new CommandExecutionException("Incorrect port value.");
            }
            return p;
        } catch (NumberFormatException ignored) {
            throw new CommandExecutionException("Incorrect port value.");
        }
    }
}
