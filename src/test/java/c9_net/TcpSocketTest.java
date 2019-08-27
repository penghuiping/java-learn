package c9_net;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.junit.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author: penghuiping
 * @date: 2019/6/28 18:04
 * @description:
 */
public class TcpSocketTest {


    @Test
    public void BioTcpServer() throws Exception {
        ServerSocket serverSocket = new ServerSocket();
        SocketAddress socketAddress = new InetSocketAddress(8080);
        serverSocket.bind(socketAddress);
        while (true) {
            var socket = serverSocket.accept();
            System.out.println("accept");
            new Thread(() -> {
                try {
                    var in = socket.getInputStream();
                    var out = socket.getOutputStream();

                    var buf = new byte[128];

                    var len = -1;
                    while ((len = in.read(buf, 0, buf.length)) > 0) {
                        out.write(buf, 0, len);
                        var tmp = new String(buf, 0, len);
                        if (tmp.contains("quit")) {
                            break;
                        }
                    }
                    out.flush();
                    in.close();
                    out.close();
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }

    @Test
    public void aioTcpServer() throws Exception {
        SocketAddress socketAddress = new InetSocketAddress(8080);
        ExecutorService executorService = Executors.newFixedThreadPool(20);
        var group = AsynchronousChannelGroup.withThreadPool(executorService);
        var asynChannel = AsynchronousServerSocketChannel.open(group).bind(socketAddress);

        var hander = new CompletionHandler<AsynchronousSocketChannel, Object>() {
            @Override
            public void completed(AsynchronousSocketChannel result, Object attachment) {
                asynChannel.accept(null, this);
                System.out.println("当前线程" + Thread.currentThread().getName());
                try {
                    if (null != result && result.isOpen()) {
                        var buf = ByteBuffer.allocate(8);
                        var receiveLength = 0;
                        while (receiveLength >= 0) {
                            var tmp = result.read(buf);
                            receiveLength = tmp.get();
                            buf.flip();
                            result.write(buf);
                            var cmd = new String(buf.array(), 0, receiveLength);
                            if (cmd.contains("quit")) {
                                break;
                            }
                            buf.clear();
                        }
                        result.close();
                    }
                } catch (Exception e) {

                } finally {

                }

            }

            @Override
            public void failed(Throwable exc, Object attachment) {

            }
        };
        asynChannel.accept(null, hander);

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String input = scanner.next();
            if (input.contains("quit")) {
                break;
            }
        }

        group.shutdownNow();
        asynChannel.close();
    }


    @Test
    public void nioTcpServer() throws Exception {
        NioEventLoopGroup boss = new NioEventLoopGroup(1);
        NioEventLoopGroup group = new NioEventLoopGroup(Runtime.getRuntime().availableProcessors());
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(boss, group)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer() {
                        @Override
                        protected void initChannel(Channel channel) {
                            channel.pipeline()
                                    .addLast(new SimpleChannelInboundHandler<ByteBuf>() {
                                        @Override
                                        protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
                                            System.out.println(new String(Unpooled.copiedBuffer(msg).array()));
                                            ctx.close();
                                        }
                                    });
                        }
                    });

            ChannelFuture f = b.bind(8080).sync();
            System.out.println(" started and listen on " + f.channel().localAddress());
            f.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully().sync();
            boss.shutdownGracefully().sync();
        }

    }
}
