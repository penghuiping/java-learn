package c7_thread;

import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.EventExecutorGroup;
import io.netty.util.concurrent.Future;
import org.junit.Test;

import java.util.concurrent.Executors;

/**
 * @author: penghuiping
 * @date: 2019/7/4 16:14
 * @description:
 */
public class ThreadTest {

    @Test
    public void singleThread() {
        new Thread(() -> {
            System.out.println("这是thread_A");
        }).start();

        new Thread(() -> {
            System.out.println("这是thread_B");
        }).start();

    }


    @Test
    public void threadPool1() {
        Executors.newFixedThreadPool(3, Executors.defaultThreadFactory());
    }


    @Test
    public void threadPool2() throws Exception {
        EventExecutorGroup group = new DefaultEventExecutorGroup(4); // 4 threads
        Future<String> f = group.submit(() -> {
            System.out.println(Thread.currentThread().getName());
            return "hello world";
        });
        f.addListener(future -> {
            System.out.println(Thread.currentThread().getName());
            System.out.println(future.getNow());
        });

        Future<String> f1 = group.submit(() -> {
            System.out.println(Thread.currentThread().getName());
            return "hello world111";
        });
        f1.addListener(future -> {
            System.out.println(Thread.currentThread().getName());
            System.out.println(future.getNow());
        });


        f.await();
        f1.await();
        group.shutdownGracefully().sync();

    }
}
