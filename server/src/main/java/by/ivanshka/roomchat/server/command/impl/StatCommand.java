package by.ivanshka.roomchat.server.command.impl;

import by.ivanshka.roomchat.common.command.Command;
import by.ivanshka.roomchat.server.service.ClientService;
import by.ivanshka.roomchat.server.service.RoomService;
import by.ivanshka.roomchat.server.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class StatCommand implements Command {
    private static final String COMMAND_STRING = "stat";
    private static final String HELP_STRING = "Prints server statistics: count of rooms, users, etc.";
    private static final String FULL_HELP_TEXT = """
            Command:
                /STAT
            Description:
                Prints server statistics: count of rooms, users, etc.
            """;

    private final RoomService roomService;
    private final ClientService clientService;
    private final StatisticsService statisticsService;

    @Override
    public String getCommandString() {
        return COMMAND_STRING;
    }

    @Override
    public void execute(List<String> args) {
        String tablePattern = "%50s : %d";

        int roomsAmount = statisticsService.getRoomsAmount();
        int chattingUsersAmount = statisticsService.getChattingClientsAmount();
        int totalUsersAmount = statisticsService.getClientsAmount();

        log.info(String.format(tablePattern, "Rooms amount", roomsAmount));
        log.info(String.format(tablePattern, "Total users amount", totalUsersAmount));
        log.info(String.format(tablePattern, "Total chatting users", chattingUsersAmount));
        log.info(String.format(tablePattern, "Total users outside the rooms", totalUsersAmount - chattingUsersAmount));
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
