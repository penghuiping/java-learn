package c14_spring;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import java.nio.ByteBuffer;
import java.nio.channels.Channels;

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
        while(true) {
            int res = readChannel.read(byteBuffer);
            if(res<=0) {
                break;
            }
            sb.append(new String(byteBuffer.array(),0,res));
            byteBuffer.clear();
        }
        log.info("file content:{}",sb.toString());
    }
}
