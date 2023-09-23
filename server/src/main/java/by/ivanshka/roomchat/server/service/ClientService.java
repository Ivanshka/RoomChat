package by.ivanshka.roomchat.server.service;

import by.ivanshka.roomchat.common.network.packet.impl.MessagePacket;
import by.ivanshka.roomchat.common.network.packet.operation.Operation;
import by.ivanshka.roomchat.common.network.packet.operation.OperationResultPacket;
import by.ivanshka.roomchat.common.network.packet.operation.impl.ChangeUsernamePacket;
import by.ivanshka.roomchat.server.chat.Client;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ClientService {
    public void changeUsername(ChangeUsernamePacket packet, Client client) {
        String oldUsername = client.getUsername();

        client.setUsername(packet.getNewUsername());

        client.getRoom()
                .ifPresent((r) -> {
                    MessagePacket notification = new MessagePacket("Server",
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
}
