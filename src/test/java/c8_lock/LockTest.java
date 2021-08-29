package c8_lock;

import org.junit.Test;

import java.util.LinkedList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author: penghuiping
 * @date: 2019/7/3 15:56
 * @description:
 */
public class LockTest {

    /**
     * 生产者消费者模型
     */
    @Test
    public void producerAndConsumerPattern() {
        Lock lock = new ReentrantLock();
        var basketFullCondition = lock.newCondition();
        var basketEmptyCondition = lock.newCondition();
        var basketMaxSize = 10;
        var basket = new LinkedList<>();

        //生产苹果 1秒生产一个
        new Thread(() -> {
            while (true) {
                lock.lock();
                try {
                    basket.push("1");
                    basketEmptyCondition.signalAll();
                    System.out.println("成功生成1个，目前篮子里有" + basket.size() + "个");
                    //这里要用while,因为唤醒以后需要重新在一次判断条件是否满足，很可能因为signalAll(),其他的生产者线程先一步已经把苹果生产好了，那么篮子又满了，你需要继续等待,
                    while (basket.size() >= basketMaxSize) {
                        try {
                            basketFullCondition.await();
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }
                } finally {
                    lock.unlock();
                }

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }

        }).start();


        //消费苹果  5秒消费一个
        new Thread(() -> {
            while (true) {
                lock.lock();
                try {
                    basket.pop();
                    basketFullCondition.signalAll();
                    System.out.println("成功消费1个，目前篮子里有" + basket.size() + "个");
                    while (basket.size() <= 0) {
                        try {
                            basketEmptyCondition.await();
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }
                } finally {
                    lock.unlock();
                }

                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }

            }
        }).start();


        for (; ; ) ;

    }


    private class Counter {
        Integer count;

        public Integer getCount() {
            return count;
        }

        public void setCount(Integer count) {
            this.count = count;
        }
    }


    /**
     * 线程3等待线程1与线程2执行完成以后执行
     *
     * @throws Exception
     */
    @Test
    public void testWaitCondition() throws Exception {
        int cpu = Runtime.getRuntime().availableProcessors();
        ExecutorService executorService = Executors.newFixedThreadPool(cpu + 1);
        Lock lock = new ReentrantLock();
        Condition countZero = lock.newCondition();
        Counter count = new Counter();
        count.setCount(2);
        CountDownLatch countDownLatch = new CountDownLatch(3);

        //线程1
        executorService.submit(() -> {
            lock.lock();
            try {
                count.setCount(count.getCount() - 1);
                countZero.signalAll();
            } finally {
                System.out.println("线程1执行完成");
                lock.unlock();
            }
            countDownLatch.countDown();
        });

        //线程2
        executorService.submit(() -> {
            lock.lock();
            try {
                count.setCount(count.getCount() - 1);
                countZero.signalAll();
            } finally {
                System.out.println("线程2执行完成");
                lock.unlock();
            }
            countDownLatch.countDown();
        });

        //线程3
        executorService.submit(() -> {
            lock.lock();
            try {
                while (count.getCount() > 0) {
                    try {
                        countZero.await(5, TimeUnit.SECONDS);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
                System.out.println("线程3执行业务逻辑,count:" + count.getCount());
            } finally {
                lock.unlock();
            }
            countDownLatch.countDown();
        });

        countDownLatch.await();
    }
}
