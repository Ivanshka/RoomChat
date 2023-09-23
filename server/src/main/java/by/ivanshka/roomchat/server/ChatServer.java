package by.ivanshka.roomchat.server;

import by.ivanshka.roomchat.common.network.decoder.PacketDecoder;
import by.ivanshka.roomchat.common.network.encoder.PacketEncoder;
import by.ivanshka.roomchat.server.network.NetworkEventHandler;
import by.ivanshka.roomchat.server.service.ClientService;
import by.ivanshka.roomchat.server.service.RoomService;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChatServer {
    @Value("${roomchat.server.port}")
    private int SERVER_PORT = 8080;
    private final RoomService roomService;
    private final ClientService clientService;
    private ServerBootstrap server;
    private ChannelFuture channelFuture;

    public void run() {

//        new Thread(() -> {
            EventLoopGroup bossGroup = new NioEventLoopGroup(1); // thread pool for clients that are connecting
            EventLoopGroup workerGroup = new NioEventLoopGroup(); // thread pool for handling clients requests (network events)

            try {
                server = new ServerBootstrap();
                server.group(bossGroup, workerGroup)
                        .channel(NioServerSocketChannel.class)
                        .childHandler(new ChannelInitializer<SocketChannel>() {
                            @Override
                            public void initChannel(SocketChannel ch) throws Exception {
                                ch.pipeline()
                                        .addLast(
                                                new PacketDecoder(),
                                                new PacketEncoder(),
                                                new NetworkEventHandler(roomService, clientService)
                                        );
                            }
                        })
                        .option(ChannelOption.SO_BACKLOG, 128)
                        .childOption(ChannelOption.SO_KEEPALIVE, true);

                try {
                    channelFuture = server.bind(SERVER_PORT).sync();
                    channelFuture.channel().closeFuture().sync();
                } catch (InterruptedException e) {
                    log.error("Chat server work was interrupted", e);
                    Thread.currentThread().interrupt();
                    throw new RuntimeException(e);
                }
            } finally {
                bossGroup.shutdownGracefully();
                workerGroup.shutdownGracefully();
            }
//        }).start();

    }
}
