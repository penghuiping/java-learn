package c2_collections;

import org.junit.Test;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author: penghuiping
 * @date: 2019/6/27 10:16
 * @description:
 */
public class SetTest {

    @Test
    public void test() {
        var set0 = Set.of(1,3,2,4,5,7);
        System.out.println(set0);
        var set1 = set0.stream().sorted().collect(Collectors.toSet());
        System.out.println(set1);
        set1.add(1);
        set1.add(2);
        System.out.println(set1);
    }
}
