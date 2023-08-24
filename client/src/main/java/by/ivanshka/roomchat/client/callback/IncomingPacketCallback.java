package by.ivanshka.roomchat.client.callback;

import by.ivanshka.roomchat.common.network.packet.Packet;

@FunctionalInterface
public interface IncomingPacketCallback {
    void handleIncomingPacket(Packet packet);
}
