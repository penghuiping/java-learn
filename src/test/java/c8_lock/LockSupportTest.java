package c8_lock;

import org.junit.Test;

import java.util.Scanner;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.LockSupport;

/**
 * @author penghuiping
 * @date 2022/7/22 20:03
 */
public class LockSupportTest {

    @Test
    public void test() {

        Thread thread0 = new Thread(()->{
            LockSupport.park(Thread.currentThread());
            System.out.println("thread0");
        });

        Thread thread1 = new Thread(()->{
            LockSupport.park(Thread.currentThread());
            System.out.println("thread1");
        });

        thread0.start();
        thread1.start();

        LockSupport.unpark(thread0);
        LockSupport.unpark(thread1);

        Scanner scanner  =new Scanner(System.in);
        scanner.hasNext();
    }
}
