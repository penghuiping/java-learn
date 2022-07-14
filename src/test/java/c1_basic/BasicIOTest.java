package c1_basic;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.util.Scanner;

/**
 * 控制台输入输出
 *
 * @author penghuiping
 * @date 2019/7/3 23:35
 */
public class BasicIOTest {

    @Test
    public void systemInTest() {
        System.setIn(new ByteArrayInputStream("hello".getBytes()));
        var scan = new Scanner(System.in);
        if (scan.hasNext()) {
            System.out.println("-------");
            System.out.println("xxx:" + scan.next());
        }
    }

    @Test
    public void systemOutTest() {
        System.out.println("hello world");
        System.out.format("你再说是:%s", "hello");
    }
}
