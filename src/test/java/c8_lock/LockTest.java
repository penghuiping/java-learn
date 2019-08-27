package c8_lock;

import org.junit.Test;

import java.util.LinkedList;
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
                }finally {
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
                }finally {
                    lock.unlock();
                }
            }
        }).start();


        for (; ; ) ;

    }
}
