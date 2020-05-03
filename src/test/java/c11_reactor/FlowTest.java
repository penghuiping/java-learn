package c11_reactor;

import org.junit.Test;

import java.util.concurrent.Flow;

/**
 * @author penghuiping
 * @date 2020/5/3 22:36
 */
public class FlowTest {

    public interface GenerateData {
        int get();
    }

    /**
     * publisher 生产者
     * subscriber 消费者
     * subscription 用于解决生产者与消费者直接的通讯机制，比如被压机制
     */
    @Test
    public void test() {

        //用于解决生产者与消费者直接的通讯机制，比如被压机制
        var subscription = new Flow.Subscription() {
            private Flow.Subscriber<? super Integer> subscriber;

            private GenerateData generateData;

            public void setSubscriber(Flow.Subscriber<? super Integer> subscriber) {
                this.subscriber = subscriber;
            }

            public void setGenerateData(GenerateData generateData) {
                this.generateData = generateData;
            }

            @Override
            public void request(long n) {
                for(int i=0;i<n;i++) {
                    subscriber.onNext(generateData.get());
                }
                subscriber.onComplete();
            }

            @Override
            public void cancel() {
                subscriber.onComplete();
            }
        };

        //生产者
        var publisher = new Flow.Publisher<Integer>() {
            @Override
            public void subscribe(Flow.Subscriber<? super Integer> subscriber) {
                subscription.setSubscriber(subscriber);
                subscription.setGenerateData(() -> {
                    return (int)(Math.random()*100);
                });
                subscriber.onSubscribe(subscription);
            }
        };

        //消费者
        publisher.subscribe(new Flow.Subscriber<Integer>() {
            @Override
            public void onSubscribe(Flow.Subscription subscription) {
                subscription.request(5);
            }

            @Override
            public void onNext(Integer item) {
                System.out.println(item);
            }

            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onComplete() {
                System.out.println("完成");
            }
        });


    }
}
