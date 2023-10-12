package by.ivanshka.roomchat.server.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
public class StatisticsService {
    private final ClientService clientService;
    private final RoomService roomService;

    public int getClientsAmount() {
        return clientService.countClients();
    }

    public int getChattingClientsAmount() {
        return clientService.countChattingClients();
    }

    public int getRoomsAmount() {
        return roomService.countRooms();
    }
}
