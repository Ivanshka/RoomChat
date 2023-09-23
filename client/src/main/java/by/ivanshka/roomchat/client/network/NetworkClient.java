package by.ivanshka.roomchat.client.network;

import by.ivanshka.roomchat.client.callback.ExceptionHandlerCallback;
import by.ivanshka.roomchat.client.callback.IncomingPacketCallback;
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
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Getter
@Component
public class NetworkClient {
    @Setter
    @Value("${room-chat.client.host}")
    private String host;
    @Setter
    @Value("${room-chat.client.port}")
    private int port;
    private SocketChannel networkChannel;

    public void connect(IncomingPacketCallback incomingPacketCallback, ExceptionHandlerCallback exceptionHandlerCallback) {

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
                                new NetworkEventHandler(incomingPacketCallback, exceptionHandlerCallback)
                        );
                    }
                });

                ChannelFuture f = clientBootstrap.connect(host, port).sync();
                f.channel().closeFuture().sync();

            } catch (InterruptedException e) {
                log.error("Can't sync() ChannelFuture", e);
                throw new RuntimeException(e);
            } finally {
                workerGroup.shutdownGracefully();
            }
        }).start();
    }

    public void sendPacket(Packet packet) {
        networkChannel.writeAndFlush(packet);
    }

    public void disconnect() {
        networkChannel.close();
    }
}
