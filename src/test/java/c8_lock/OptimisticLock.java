package c8_lock;

import org.junit.Test;
import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author: penghuiping
 * @date: 2019/7/5 11:10
 * @description: 使用乐观锁 CAS volatile
 */
public class OptimisticLock {

    private static final Unsafe unsafe = createUnsafe();

    private volatile int count = 0;

    private static final long VALUE = unsafe.objectFieldOffset(getValueField());

    private static Field getValueField() {
        try {
            return OptimisticLock.class.getDeclaredField("count");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static Unsafe createUnsafe() {
        try {
            Class<?> unsafeClass = Class.forName("sun.misc.Unsafe");
            Field field = unsafeClass.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            Unsafe unsafe = (Unsafe) field.get(null);
            return unsafe;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void increaseWithoutCAS() {
        count++;
    }

    public void increaseWithCAS() {
        while (true) {
           boolean result =  unsafe.compareAndSwapInt(this, VALUE, count, count + 1);
           if(result) {
               break;
           }
        }
    }

    @Test
    public void test() throws Exception {
        ExecutorService executorService = new ThreadPoolExecutor(10, 20,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>());

        CountDownLatch countDownLatch1 = new CountDownLatch(10000);
        for (int i = 0; i < 10000; i++) {
            executorService.submit(() -> {
                increaseWithCAS();
                countDownLatch1.countDown();
            });
        }
        countDownLatch1.await();

        System.out.println("加了乐观锁以后的情况,count:" + count);

        count = 0;

        CountDownLatch countDownLatch2 = new CountDownLatch(10000);
        for (int i = 0; i < 10000; i++) {
            executorService.submit(() -> {
                increaseWithoutCAS();
                countDownLatch2.countDown();
            });
        }
        countDownLatch2.await();

        System.out.println("没有加乐观锁以后的情况,count:" + count);
    }

}
