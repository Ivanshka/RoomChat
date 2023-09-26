package by.ivanshka.roomchat.client.command;

import java.util.List;

public interface Command {
    String getCommandString();
    void execute(List<String> args);
    String getShortHelpString();
    String getFullHelpText();
}
