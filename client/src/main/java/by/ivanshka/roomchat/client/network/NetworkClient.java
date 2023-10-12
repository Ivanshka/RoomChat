package by.ivanshka.roomchat.client.network;

import by.ivanshka.roomchat.client.callback.IncomingPacketCallback;
import by.ivanshka.roomchat.common.exception.handler.ExceptionHandler;
import by.ivanshka.roomchat.client.exception.impl.DisconnectedException;
import by.ivanshka.roomchat.common.network.decoder.PacketDecoder;
import by.ivanshka.roomchat.common.network.encoder.PacketEncoder;
import by.ivanshka.roomchat.common.network.packet.Packet;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Getter
@Component
@RequiredArgsConstructor
public class NetworkClient {
    private final IncomingPacketCallback incomingPacketCallback;
    private final ExceptionHandler exceptionHandler;
    private SocketChannel networkChannel;
    private volatile boolean isConnected;

    public void connect(String host, int port) {
        if (isConnected) {
            log.warn("Connected already, you have to disconnect before connecting to another server");
            return;
        }

        new Thread(() -> {

            EventLoopGroup workerGroup = new NioEventLoopGroup(1);

            try {
                Bootstrap clientBootstrap = new Bootstrap();
                clientBootstrap.group(workerGroup);
                clientBootstrap.channel(NioSocketChannel.class);
                clientBootstrap.option(ChannelOption.SO_KEEPALIVE, true);
                clientBootstrap.handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch) {
                        networkChannel = ch;
                        ch.pipeline().addLast(
                                new PacketEncoder(),
                                new PacketDecoder(),
                                new NetworkEventHandler(incomingPacketCallback, exceptionHandler)
                        );
                    }
                });

                ChannelFuture f = clientBootstrap.connect(host, port).sync();
                isConnected = true;
                f.channel().closeFuture().sync();

            } catch (InterruptedException e) {
                log.error("Can't sync() ChannelFuture", e);
                throw new RuntimeException(e);
            } finally {
                workerGroup.shutdownGracefully();
                log.info("Disconnected");
            }
        }, "NetworkThread").start();
    }

    public void sendPacket(Packet packet) {
        requireConnection();
        networkChannel.writeAndFlush(packet);
    }

    public void disconnect() {
        if (isConnected) {
            networkChannel.close();
            isConnected = false;
        } else {
            throw new DisconnectedException();
        }
    }

    private void requireConnection() {
        if (!isConnected()) {
            throw new DisconnectedException();
        }
    }
}
