package c8_lock;

import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author penghuiping
 * @date 2019/12/10 09:35
 */
public class ReadWriteLockTest {

    private Integer value=0;


    @Test
    public void test() throws Exception{
        CountDownLatch countDownLatch = new CountDownLatch(210);
        ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

        for(int i=0;i<200;i++) {
            new Thread(()->{
                readWriteLock.writeLock().lock();
                writeAction();
                readWriteLock.writeLock().unlock();
                countDownLatch.countDown();
            }).start();
        }

        for(int i=0;i<10;i++) {
            new Thread(()->{
                readWriteLock.readLock().lock();
                readAction();
                readWriteLock.readLock().unlock();
                countDownLatch.countDown();
            }).start();

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }



        countDownLatch.await();
    }

    public void readAction() {
        System.out.println("value的值为:"+this.value);
    }

    public void writeAction() {
        this.value++;
    }



}
