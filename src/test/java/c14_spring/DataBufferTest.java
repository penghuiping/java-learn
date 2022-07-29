package c14_spring;

import io.netty.buffer.PooledByteBufAllocator;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.core.io.buffer.NettyDataBufferFactory;
import reactor.core.publisher.Flux;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.CountDownLatch;

/**
 * @author penghuiping
 * @date 2022/7/27 23:26
 */
@Slf4j
public class DataBufferTest {

    @Test
    public void test() {
        DataBufferFactory dataBufferFactory = new DefaultDataBufferFactory(true);
        DataBuffer dataBuffer = dataBufferFactory.allocateBuffer(1024);
        try {
            dataBuffer.write("你好！世界", StandardCharsets.UTF_8);
            log.info("内容:{}", dataBuffer.toString(StandardCharsets.UTF_8));
        } finally {
            DataBufferUtils.release(dataBuffer);
        }
    }

    @Test
    public void test1() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        NettyDataBufferFactory dataBufferFactory = new NettyDataBufferFactory(new PooledByteBufAllocator(true));
        Flux<DataBuffer> dataBufferFlux = DataBufferUtils.read(new ClassPathResource("logback.xml"), dataBufferFactory, 8);
        DataBufferUtils.join(dataBufferFlux).subscribe(dataBuffer -> {
            log.info("内容:{}", dataBuffer.toString(StandardCharsets.UTF_8));
            DataBufferUtils.release(dataBuffer);
        }, null, countDownLatch::countDown);
        countDownLatch.await();
    }
}
