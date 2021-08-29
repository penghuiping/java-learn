package c7_thread;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.EventExecutorGroup;
import io.netty.util.concurrent.Future;
import org.junit.Test;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

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
        CountDownLatch countDownLatch = new CountDownLatch(1);
        int cpu = Runtime.getRuntime().availableProcessors();
        ThreadFactory threadFactory = new ThreadFactoryBuilder().setNameFormat("test-name-%d").build();
        ExecutorService pool = new ThreadPoolExecutor(cpu + 1, 2 * cpu,
                0L, TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<>(1024), threadFactory, new ThreadPoolExecutor.AbortPolicy()
        );

        for(int i=0;i<3;i++) {
            pool.submit(() -> {
                try {
                    int j = 1/0;
                    System.out.println("thread name:" + Thread.currentThread().getName());
                } catch(Exception e) {
                    e.printStackTrace();
                }finally {
                    countDownLatch.countDown();
                }
            });
        }


        pool.shutdown();
        try {
            pool.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }


    /**
     * netty的线程池
     *
     * @throws Exception
     */
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


    /**
     * 线程优雅退出
     *
     * @throws Exception
     */
    @Test
    public void thread3() throws Exception {
        AtomicBoolean interrupted = new AtomicBoolean(false);
        Thread workerThread = new Thread(() -> {
            while (interrupted.get()) {
                try {
                    System.out.println("1");
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Thread.currentThread().interrupt();
                }
            }
        });
        workerThread.start();


        if (workerThread.isAlive()) {
            System.out.println("alive");
            Thread.sleep(2000);
            interrupted.compareAndSet(false, true);
            workerThread.interrupt();
            try {
                workerThread.join(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }
        }
    }
}
