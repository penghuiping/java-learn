package c7_thread;

import org.junit.Test;

/**
 * @author: penghuiping
 * @date: 2019/7/4 16:14
 * @description:
 */
public class ThreadTest {

    @Test
    public void singleThread() {
        new Thread(()->{
            System.out.println("这是thread_A");
        }).start();

        new Thread(()->{
            System.out.println("这是thread_B");
        }).start();

    }


    @Test
    public void threadPool() {

    }


}
