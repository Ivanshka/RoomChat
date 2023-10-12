package by.ivanshka.roomchat.server;

import by.ivanshka.roomchat.common.exception.handler.ExceptionHandler;
import by.ivanshka.roomchat.common.network.decoder.PacketDecoder;
import by.ivanshka.roomchat.common.network.encoder.PacketEncoder;
import by.ivanshka.roomchat.server.network.NetworkEventHandler;
import by.ivanshka.roomchat.server.service.ClientService;
import by.ivanshka.roomchat.server.service.RoomService;
import io.netty.bootstrap.ServerBootstrap;
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
    private final RoomService roomService;
    private final ClientService clientService;
    private final ExceptionHandler exceptionHandler;

    @Value("${room-chat.server.port:8080}")
    private int port;
    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;
    private volatile boolean isRun;

    public boolean isRun() {
        return isRun;
    }

    public void start() {

        new Thread(() -> {
            bossGroup = new NioEventLoopGroup(1); // thread pool for clients that are connecting
            workerGroup = new NioEventLoopGroup(); // thread pool for handling clients requests (network events)

            try {
                ServerBootstrap server = new ServerBootstrap();
                server.group(bossGroup, workerGroup)
                        .channel(NioServerSocketChannel.class)
                        .childHandler(new ChannelInitializer<SocketChannel>() {
                            @Override
                            public void initChannel(SocketChannel ch) throws Exception {
                                ch.pipeline()
                                        .addLast(
                                                new PacketDecoder(),
                                                new PacketEncoder(),
                                                new NetworkEventHandler(roomService, clientService, exceptionHandler)
                                        );
                            }
                        })
                        .option(ChannelOption.SO_BACKLOG, 128)
                        .childOption(ChannelOption.SO_KEEPALIVE, true);

                isRun = true;

                server.bind(port)
                        .sync()
                        .channel()
                        .closeFuture()
                        .sync();

            } catch (InterruptedException e) {
                log.error("Can't sync(), thread was interrupted. Server will be shutted down.", e);
                stop();
            }
        }, "ServerThread").start();

    }

    public void stop() {
        log.warn("Server shutdown was initiated...");
        isRun = false;
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
        log.warn("Server shutted down.");
    }
}
