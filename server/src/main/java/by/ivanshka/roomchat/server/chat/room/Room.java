package by.ivanshka.roomchat.server.chat.room;

import by.ivanshka.roomchat.common.network.packet.Packet;
import by.ivanshka.roomchat.server.chat.Client;
import lombok.Getter;

import java.util.List;

@Getter
public abstract class Room {
    protected String id;
    protected List<Client> clients;

    public void broadcast(Packet packet) {
        clients.parallelStream().forEach(client -> client.sendPacket(packet));
    }
}
