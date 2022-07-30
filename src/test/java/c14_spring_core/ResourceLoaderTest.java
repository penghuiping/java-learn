package c14_spring_core;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import reactor.core.publisher.Flux;

import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.charset.StandardCharsets;

/**
 * @author penghuiping
 * @date 2022/7/26 21:14
 */
@Slf4j
public class ResourceLoaderTest {

    @Test
    public void test() throws Exception{
        var readChannel = Channels.newChannel(new ClassPathResource("logback.xml").getInputStream());
        ByteBuffer byteBuffer =  ByteBuffer.allocate(1024);
        StringBuilder sb = new StringBuilder();
        while (true) {
            int res = readChannel.read(byteBuffer);
            if (res <= 0) {
                break;
            }
            sb.append(new String(byteBuffer.array(), 0, res));
            byteBuffer.clear();
        }
        log.info("file content:{}", sb);
    }

    @Test
    public void test1() throws Exception {
        PathMatchingResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
        Resource resource = resourcePatternResolver.getResource("classpath:logback.xml");
        Flux<DataBuffer> dataBufferFlux = DataBufferUtils.read(resource, new DefaultDataBufferFactory(), 128);
        DataBuffer dataBuffer = DataBufferUtils.join(dataBufferFlux).block();
        log.info("file content:{}", dataBuffer.toString(StandardCharsets.UTF_8));
        DataBufferUtils.release(dataBuffer);
    }
}
