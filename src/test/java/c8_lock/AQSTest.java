package c8_lock;

import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;

/**
 * @author penghuiping
 * @date 2022/7/21 20:53
 */
public class AQSTest {
    int k=0;

    private static class Sync extends AbstractQueuedSynchronizer {
        protected Sync() {
            setState(0);
        }

        @Override
        protected boolean tryAcquire(int arg) {
             if(compareAndSetState(0,1)) {
                 setExclusiveOwnerThread(Thread.currentThread());
                 return true;
             }
             return false;
        }

        @Override
        protected boolean tryRelease(int arg) {
            if(getExclusiveOwnerThread() == Thread.currentThread()) {
                setExclusiveOwnerThread(null);
                setState(0);
                return true;
            }
            return false;
        }
    }

    Sync sync = new Sync();

    private void lock() {
        sync.acquire(1);
    }

    private void unlock() {
        sync.release(1);
    }

    @Test
    public void test() throws Exception{
        CountDownLatch countDownLatch = new CountDownLatch(10000);
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        for(int i=0;i<10000;i++) {
            executorService.submit(()->{
                this.lock();
                k=k+1;
                this.unlock();
                countDownLatch.countDown();
            });
        }
        countDownLatch.await();
        System.out.println("k:"+k);
    }
}
