package by.ivanshka.roomchat.server.service;

import by.ivanshka.roomchat.common.network.packet.impl.MessagePacket;
import by.ivanshka.roomchat.common.network.packet.operation.Operation;
import by.ivanshka.roomchat.common.network.packet.operation.OperationResultPacket;
import by.ivanshka.roomchat.common.network.packet.operation.impl.ChangeUsernamePacket;
import by.ivanshka.roomchat.server.chat.Client;
import by.ivanshka.roomchat.server.network.NetworkEventHandler;
import by.ivanshka.roomchat.server.storage.ClientStorage;
import io.netty.channel.Channel;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ClientService {
    private final ClientStorage clientStorage;
    private final RoomService roomService;

    @PostConstruct
    void subscribingTheEvents() {
        NetworkEventHandler.addConnectedEventHandler(ctx -> clientStorage.save(ctx.channel()));

        NetworkEventHandler.addDisconnectedEventHandler(ctx -> {
            Channel channel = ctx.channel();
            Client disconnectedClient = clientStorage.getClientByChannel(channel);
            roomService.removeFromRoomIfJoined(disconnectedClient);
            clientStorage.remove(channel);
        });
    }

    @Value("${room-chat.server.public-title}")
    private String serverTitle;

    public void changeUsername(ChangeUsernamePacket packet, Client client) {
        String oldUsername = client.getUsername();

        client.setUsername(packet.getNewUsername());

        client.getRoom()
                .ifPresent((r) -> {
                    MessagePacket notification = new MessagePacket(serverTitle,
                            String.format("Client \"%s\" changed his username to \"%s\"",
                                    oldUsername,
                                    packet.getNewUsername())
                    );
                    r.broadcast(notification);
                });

        OperationResultPacket resultPacket = new OperationResultPacket(true, Operation.CHANGE_USERNAME,
                List.of(client.getUsername())
        );

        client.sendPacket(resultPacket);
    }

    public int countClients() {
        return clientStorage.count();
    }

    public int countChattingClients() {
        return clientStorage.countChatting();
    }

    public Client getClientByChannel(Channel channel) {
        return clientStorage.getClientByChannel(channel);
    }
}
