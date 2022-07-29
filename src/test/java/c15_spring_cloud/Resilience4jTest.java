package c15_spring_cloud;

import io.github.resilience4j.bulkhead.Bulkhead;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.decorators.Decorators;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.time.Duration;
import java.util.Random;
import java.util.concurrent.ExecutionException;

/**
 * @author penghuiping
 * @date 2022/7/28 17:56
 */
@Slf4j
public class Resilience4jTest {
    long count = 0;

    @Test
    public void test() throws ExecutionException, InterruptedException {
        CircuitBreakerConfig circuitBreakerConfig = CircuitBreakerConfig.custom()
                .failureRateThreshold(50f)
                .slowCallDurationThreshold(Duration.ofSeconds(5))
                .slowCallRateThreshold(100f)
                .waitDurationInOpenState(Duration.ofSeconds(60))
                .slidingWindow(100, 10, CircuitBreakerConfig.SlidingWindowType.TIME_BASED)
                .build();
        CircuitBreaker circuitBreaker = CircuitBreaker.of("backendService", circuitBreakerConfig);
        Bulkhead bulkhead = Bulkhead.ofDefaults("backendService");
        Random random = new Random();
        while (true) {
            long now = System.currentTimeMillis();
            var result = Decorators.ofSupplier(() -> {
                        int value = 0;
                        if (count < 100) {
                            value = random.nextInt(2000, 10000);
                        } else {
                            value = 1000;
                        }

                        try {
                            Thread.sleep(value);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        return "hello";
                    }).withCircuitBreaker(circuitBreaker)
                    .withBulkhead(bulkhead)
                    .withFallback((s, throwable) -> {
                        return "fallback";
                    })
                    .get();
            log.info("耗时:{}ms", System.currentTimeMillis() - now);
            ++count;
            log.info("res:{},count:{}", result, count);
            Thread.sleep(1000);
        }

    }
}
