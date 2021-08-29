package c8_lock;

import org.junit.Test;
import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

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
            //重试直到成功为止
            boolean result = unsafe.compareAndSwapInt(this, VALUE, count, count + 1);
            if (result) {
                break;
            }
        }
    }

    @Test
    public void test() throws Exception {
        ExecutorService pool = Executors.newFixedThreadPool(10);

        CountDownLatch countDownLatch1 = new CountDownLatch(10000);
        for (int i = 0; i < 10000; i++) {
            pool.submit(() -> {
                increaseWithCAS();
                countDownLatch1.countDown();
            });
        }
        countDownLatch1.await();
        System.out.println("加了乐观锁以后的情况,count:" + count);

        count = 0;
        CountDownLatch countDownLatch2 = new CountDownLatch(10000);
        for (int i = 0; i < 10000; i++) {
            pool.submit(() -> {
                increaseWithoutCAS();
                countDownLatch2.countDown();
            });
        }
        countDownLatch2.await();
        System.out.println("没有加乐观锁以后的情况,count:" + count);
    }

    /**
     * 乐观锁保证只有一个线程执行
     *
     * @throws Exception
     */
    @Test
    public void test1() throws Exception {
        CountDownLatch countDownLatch = new CountDownLatch(1000);

        AtomicBoolean allowed = new AtomicBoolean(false);
        ExecutorService pool = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 1000; i++) {
            pool.execute(() -> {
                try {
                    boolean flag = allowed.compareAndSet(false, true);
                    if (flag && allowed.get()) {
                        System.out.println("执行...");
                    }
                } finally {
                    countDownLatch.countDown();
                }
            });
        }
        countDownLatch.await();
    }

}
