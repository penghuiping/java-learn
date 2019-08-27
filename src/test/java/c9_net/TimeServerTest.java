package c9_net;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author: penghuiping
 * @date: 2019/7/4 15:15
 * @description:
 */
public class TimeServerTest {


    public static void initServer() throws Exception {
        ExecutorService executorService = Executors.newFixedThreadPool(20);
        AsynchronousChannelGroup asynchronousChannelGroup = AsynchronousChannelGroup.withThreadPool(executorService);
        SocketAddress address = new InetSocketAddress(8081);
        var server = AsynchronousServerSocketChannel.open(asynchronousChannelGroup).bind(address);
        var serverHandler = new CompletionHandler<AsynchronousSocketChannel, Object>() {
            @Override
            public void completed(AsynchronousSocketChannel result, Object attachment) {
                server.accept(null, this);
                //处理时间服务器的逻辑
                //连接上以后立马返回当前时间，并且断开连接
                var byteBuffer = ByteBuffer.allocate(8);
                while (true) {
                    try {
                        var lenFuture = result.read(byteBuffer);
                        var len = lenFuture.get();
                        String value = new String(byteBuffer.array(), 0, len);
                        byteBuffer.clear();
                        if(value.contains("quit")){
                            break;
                        }else if(value.contains("time")) {
                            var localDateTime = LocalDateTime.now();
                            var now = localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                            var buf = ByteBuffer.wrap((now+"\r\n").getBytes());
                            result.write(buf);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        break;
                    }
                }
                try {
                    if(result.isOpen())
                        result.close();
                }catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void failed(Throwable exc, Object attachment) {
               exc.printStackTrace();
            }
        };

        server.accept(null, serverHandler);

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String cmd = scanner.next();
            if (cmd.contains("quit")) {
                break;
            }
        }

        if (server.isOpen()) {
            server.close();
        }

        if (!asynchronousChannelGroup.isShutdown()) {
            asynchronousChannelGroup.shutdownNow();
        }
    }


    public static void main(String args[]) throws Exception {
        initServer();
    }
}
