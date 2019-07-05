package lock;

import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author: penghuiping
 * @date: 2019/7/5 11:09
 * @description: 使用synchronize锁
 */
public class SynchronizedTest {
    private int count = 0;

    public synchronized void increaseWithLock() {
        count++;
    }

    public void increaseWithoutLock() {
        count++;
    }


    @Test
    public void test() throws Exception{

        ExecutorService executorService =  new ThreadPoolExecutor(10, 20,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>());

        CountDownLatch countDownLatch1 = new CountDownLatch(1000);
        for(int i=0;i<1000;i++) {
            executorService.submit(()->{
                increaseWithLock();
                countDownLatch1.countDown();
            });
        }
        countDownLatch1.await();

        System.out.println("加了同步锁以后的情况,count:"+count);

        count = 0;

        CountDownLatch countDownLatch2 = new CountDownLatch(1000);
        for(int i=0;i<1000;i++) {
            executorService.submit(()->{
                increaseWithoutLock();
                countDownLatch2.countDown();
            });
        }
        countDownLatch2.await();

        System.out.println("没有加同步锁以后的情况,count:"+count);




    }

}
