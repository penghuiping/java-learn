package c2_collections;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: penghuiping
 * @date: 2019/6/27 10:16
 * @description:
 */
public class MapTest {

    @Test
    public void test() {
        var map0 = Map.of("name","jack","age",12);
        System.out.println(map0);
        var map1 = new HashMap<>(map0);
        map1.put("height",170);
        System.out.println(map1);

        var map2 = Map.ofEntries(Map.entry("name","mary"),Map.entry("age",13));
        System.out.println(map2);
    }
}
