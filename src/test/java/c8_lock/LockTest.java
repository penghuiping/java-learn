package c8_lock;

import org.junit.Test;

import java.util.LinkedList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
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
    @Test
    public void producerAndConsumerPattern() {
        Lock lock = new ReentrantLock();

        var producerCondition = lock.newCondition();
        var consumerCondition = lock.newCondition();

        var basket = new LinkedList<>();


        //生产苹果 1秒生产一个
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(1000);
                    lock.lock();
                    basket.push("1");
                    consumerCondition.signal();
                    System.out.println("成功生成1个，目前篮子里有" + basket.size() + "个");
                    if (basket.size() >= 10) {
                        producerCondition.await();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
            }

        }).start();


        //消费苹果  5秒消费一个
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(5000);
                    lock.lock();
                    basket.pop();
                    producerCondition.signal();
                    System.out.println("成功消费1个，目前篮子里有" + basket.size() + "个");
                    if (basket.size() <= 0) {
                        consumerCondition.await();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
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
     * @throws Exception
     */
    @Test
    public void testWaitCondition() throws Exception {
        ExecutorService executorService = Executors.newFixedThreadPool(20);

        CountDownLatch countDownLatch = new CountDownLatch(200000);
        for (int i = 0; i < 200000; i++) {
            executorService.submit(() -> {
                Lock lock = new ReentrantLock();
                Condition countZero = lock.newCondition();

                Counter count = new Counter();
                count.setCount(2);

                final AtomicInteger value1 = new AtomicInteger(0);
                final AtomicInteger value2 = new AtomicInteger(0);

                executorService.submit(() -> {
                    value1.set(1);
                    try {
                        lock.lock();
                        count.setCount(count.getCount() - 1);
                        countZero.signalAll();
                    } finally {
                        lock.unlock();
                    }


                });

                executorService.submit(() -> {
                    value2.set(2);
                    try {
                        lock.lock();
                        count.setCount(count.getCount() - 1);
                        countZero.signalAll();
                    } finally {
                        lock.unlock();
                    }
                });

                executorService.submit(() -> {
                    while (true) {
                        try {
                            try {
                                lock.lock();
                                if (count.getCount() <= 0) {
                                    break;
                                } else {
                                    countZero.await();
                                }
                            } finally {
                                lock.unlock();
                            }

                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    if ((value1.get() + value2.get()) == 3) {

                    } else {
                        System.out.println("最终执行结果错误为:" + (value1.get() + value2.get()));
                    }
                    countDownLatch.countDown();
                });
            });
        }

        countDownLatch.await();
    }
}
