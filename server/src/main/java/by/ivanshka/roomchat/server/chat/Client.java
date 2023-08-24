package by.ivanshka.roomchat.server.chat;

import by.ivanshka.roomchat.common.network.packet.Packet;
import by.ivanshka.roomchat.server.chat.room.Room;
import io.netty.channel.Channel;
import lombok.Getter;
import lombok.Setter;

import java.util.Optional;

@Getter
@Setter
public class Client {
    private final Channel channel;
    private Optional<Room> room;
    private String username;

    public Client(Channel channel) {
        this.channel = channel;
        room = Optional.empty();
    }

    public void setRoom(Room room) {
        this.room = Optional.of(room);
    }

    public void sendPacket(Packet packet) {
        channel.writeAndFlush(packet);
    }

    public void disconnect() {
        channel.close();
    }
}
