package c11_reactor;

import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author: penghuiping
 * @date: 2019/8/26 17:59
 * @description:
 */
public class ReactorTest {

    @Test
    public void test1() throws Exception {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        Flux<Integer> flux = Flux.just(1, 2, 3, 4, 5, 6).map(i -> {
            System.out.println("平方计算,当前执行在线程:" + Thread.currentThread().getName());
            return i * i;
        })
                .publishOn(Schedulers.single())
                .filter(y -> {
                    System.out.println("过滤出偶数,当前执行在线程:" + Thread.currentThread().getName());
                    if (y % 2 == 0) {
                        return true;
                    } else {
                        return false;
                    }
                })
                .subscribeOn(Schedulers.elastic())
                .map(z -> {
                    System.out.println("数据减一,当前执行在线程:" + Thread.currentThread().getName());
                    return z - 1;
                });

        flux.subscribe(integer -> {
            System.out.println("==================>最后的数据位" + integer + ",当前执行在线程:" + Thread.currentThread().getName());
        }, throwable -> {

        }, () -> {
            countDownLatch.countDown();
            System.out.println("完成...");
        }, subscription -> {
            subscription.request(6);
        });
        countDownLatch.await();
    }


    @Test
    public void test2() throws Exception {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        Mono.just(new AtomicInteger(0)).publishOn(Schedulers.parallel())
                .map(integer -> integer.incrementAndGet())
                .repeat(99)
                .subscribe(result -> {
                    System.out.println("result:" + result);
                }, throwable -> {
                    throwable.printStackTrace();
                }, () -> {
                    countDownLatch.countDown();
                }, subscription -> {
                    subscription.request(100);
                });
        countDownLatch.await();
    }


    @Test
    public void test3() throws Exception {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        Mono.fromCallable(() -> {
            System.out.println("线程1为:" + Thread.currentThread().getName());
            return List.of(1, 2, 3);
        }).publishOn(Schedulers.parallel()).zipWith(Mono.fromCallable(() -> {
            System.out.println("线程2为:" + Thread.currentThread().getName());
            return List.of(4, 5);
        })).publishOn(Schedulers.parallel()).map(objects -> {
            System.out.println("线程3为:" + Thread.currentThread().getName());
            List<Integer> list = new ArrayList<>();
            list.addAll(objects.getT1());
            list.addAll(objects.getT2());
            return list;
        }).subscribeOn(Schedulers.parallel())
                .subscribe(list -> {
                    System.out.println("合并为数据：" + list);
                }, throwable -> {
                    throwable.printStackTrace();
                }, () -> {
                    countDownLatch.countDown();
                }, subscription -> {
                    subscription.request(1);
                });
        countDownLatch.await();
    }

}
