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
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public class NetworkClient {
    private final String host;
    private final int port;
    private SocketChannel networkChannel;

    public NetworkClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

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
