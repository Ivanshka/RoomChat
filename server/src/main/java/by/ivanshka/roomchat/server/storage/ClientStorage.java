package by.ivanshka.roomchat.server.storage;

import by.ivanshka.roomchat.server.chat.Client;
import by.ivanshka.roomchat.server.network.NetworkEventHandler;
import io.netty.channel.Channel;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class ClientStorage {
    private final Map<Channel, Client> clients = new ConcurrentHashMap<>();

    public Client save(Channel channel) {
        Client client = new Client(channel);
        clients.put(channel, client);
        return client;
    }

    public void remove(Channel channel) {
        clients.remove(channel);
    }

    public Client getClientByChannel(Channel channel) {
        return clients.get(channel);
    }

    public int count() {
        return clients.size();
    }

    public int countChatting() {
        return (int) clients.values()
                .stream()
                .filter(client -> client.getRoom().isPresent())
                .count();
    }
}
