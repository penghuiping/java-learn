package c2_collections;

import org.junit.Test;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;

/**
 * @author: penghuiping
 * @date: 2019/6/27 10:16
 * @description:
 */
public class ListTest {

    final static int count = 50000;

    @Test
    public void test() {
        var list0 = new ArrayList<>(List.of(1, 2, 3, 4, 5, 6, 7, 8));
        System.out.println(list0);

        list0.removeIf(a->a%2==0);
        System.out.println(list0);
    }

    @Test
    public void arrayListTest() throws Exception {
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.setSeed(System.currentTimeMillis());

        //新增操作
        long now2 = System.currentTimeMillis();
        var list = new ArrayList<Integer>();
        for (int i = 0; i < count; i++) {
            list.add(i);
        }
        long end2 = System.currentTimeMillis();
        System.out.println("ArrayList新增操作耗时为:" + (end2 - now2) + "ms");

        //随机查询
        long now = System.currentTimeMillis();
        for (int i = 0; i < count; i++) {
            int index = secureRandom.nextInt(list.size());
            list.get(index);
        }

        long end = System.currentTimeMillis();
        System.out.println("ArrayList随机删除操作耗时为:" + (end - now) + "ms");


        //随机删除
        long now1 = System.currentTimeMillis();
        for (int i = 0; i < count; i++) {
            int index = secureRandom.nextInt(list.size());
            list.remove(index);
        }

        long end1 = System.currentTimeMillis();
        System.out.println("ArrayList随机删除操作耗时为:" + (end1 - now1) + "ms");

        //并发情况下新增元素
        final var list1 = new ArrayList<Integer>();
        var executorService = Executors.newFixedThreadPool(100);
        var countdown = new CountDownLatch(10000);
        for (int i = 0; i < 10000; i++) {
            final int index = i;
            executorService.submit(() -> {
                list1.add(index);
                countdown.countDown();
            });
        }
        countdown.await();
        System.out.println("list1的元素大小为:" + list1.size());
    }

    @Test
    public void linkListTest() {
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.setSeed(System.currentTimeMillis());

        //新增操作
        long now2 = System.currentTimeMillis();
        var list = new LinkedList<Integer>();
        for (int i = 0; i < count; i++) {
            list.add(i);
        }
        long end2 = System.currentTimeMillis();
        System.out.println("LinkList新增操作耗时为:" + (end2 - now2) + "ms");


        //随机查询
        long now1 = System.currentTimeMillis();
        for (int i = 0; i < count; i++) {
            int index = secureRandom.nextInt(list.size());
            list.get(index);
        }

        long end1 = System.currentTimeMillis();
        System.out.println("LinkList随机删除操作耗时为:" + (end1 - now1) + "ms");


        //随机删除
        long now = System.currentTimeMillis();
        for (int i = 0; i < count; i++) {
            int index = secureRandom.nextInt(list.size());
            list.remove(index);
        }

        long end = System.currentTimeMillis();
        System.out.println("LinkList随机删除操作耗时为:" + (end - now) + "ms");
    }

    @Test
    public void vectorTest() throws Exception {
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.setSeed(System.currentTimeMillis());

        //新增操作
        long now2 = System.currentTimeMillis();
        var list = new Vector<Integer>();
        for (int i = 0; i < count; i++) {
            list.add(i);
        }
        long end2 = System.currentTimeMillis();
        System.out.println("Vector随机新增操作耗时为:" + (end2 - now2) + "ms");

        //随机查询
        long now = System.currentTimeMillis();
        for (int i = 0; i < count; i++) {
            int index = secureRandom.nextInt(list.size());
            list.get(index);
        }
        long end = System.currentTimeMillis();
        System.out.println("Vector随机查询操作耗时为:" + (end - now) + "ms");

        //随机删除
        long now1 = System.currentTimeMillis();
        for (int i = 0; i < count; i++) {
            int index = secureRandom.nextInt(list.size());
            list.remove(index);
        }

        long end1 = System.currentTimeMillis();
        System.out.println("Vector随机删除操作耗时为:" + (end1 - now1) + "ms");

        //并发情况下新增元素
        final var list1 = new Vector<Integer>();
        var executorService = Executors.newFixedThreadPool(100);
        var countdown = new CountDownLatch(10000);
        for (int i = 0; i < 10000; i++) {
            final int index = i;
            executorService.submit(() -> {
                list1.add(index);
                countdown.countDown();
            });
        }
        countdown.await();
        System.out.println("list1的元素大小为:" + list1.size());

    }


}
