package c9_net;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
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
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

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
                var atomicInteger = new AtomicInteger(0);
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
                            atomicInteger.addAndGet(receiveLength);
                            if (cmd.contains("quit") || atomicInteger.get() >= 30) {
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
            b.group(boss, group);
            b.channel(NioServerSocketChannel.class);
            b.localAddress(8080);
            b.option(ChannelOption.SO_KEEPALIVE,true);
            b.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel channel) {
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
            ChannelFuture f = b.bind().sync();
            System.out.println(" started and listen on " + f.channel().localAddress());
            f.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully();
            boss.shutdownGracefully();
        }

    }

    @Test
    public void aioClient() throws Exception {
        SocketAddress socketAddress = new InetSocketAddress("localhost", 8080);
        ByteBuffer byteBuffer = ByteBuffer.allocate(512);

        CountDownLatch countDownLatch = new CountDownLatch(1);

        AsynchronousSocketChannel asynchronousSocketChannel = AsynchronousSocketChannel.open();
        asynchronousSocketChannel.connect(socketAddress, null, new CompletionHandler<Void, Object>() {
            @Override
            public void completed(Void result, Object attachment) {

                while (asynchronousSocketChannel.isOpen()) {
                    byteBuffer.clear();
                    byteBuffer.put("！！！".getBytes());
                    byteBuffer.flip();

                    asynchronousSocketChannel.write(byteBuffer, null, new CompletionHandler<Integer, Object>() {
                        @Override
                        public void completed(Integer result, Object attachment) {
                            byteBuffer.clear();
                            asynchronousSocketChannel.read(byteBuffer, null, new CompletionHandler<Integer, Object>() {
                                @Override
                                public void completed(Integer result, Object attachment) {
                                    byteBuffer.flip();
                                    System.out.println(new String(byteBuffer.array(), byteBuffer.position(), byteBuffer.limit()));
                                }

                                @Override
                                public void failed(Throwable exc, Object attachment) {

                                }
                            });
                        }

                        @Override
                        public void failed(Throwable exc, Object attachment) {
                            exc.printStackTrace();
                            try {
                                asynchronousSocketChannel.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                countDownLatch.countDown();

            }

            @Override
            public void failed(Throwable exc, Object attachment) {
            }
        });


        countDownLatch.await();




    }
}
