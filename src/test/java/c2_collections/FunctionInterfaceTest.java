package c2_collections;

import org.junit.Test;

import java.util.function.Function;
import java.util.function.IntConsumer;
import java.util.function.IntPredicate;
import java.util.stream.IntStream;

/**
 * @author penghuiping
 * @date 2020/5/1 22:44
 */
public class FunctionInterfaceTest {

    /**
     * predict 一般用于测试表达是是否为真
     */
    @Test
    public void predictTest() {
        IntPredicate predicate = (int a)-> a%2==0;
        IntStream.of(1,2,3).filter(predicate).forEach(System.out::println);
    }

    /**
     * consumer用于消费流中的元素
     */
    @Test
    public void consumerTest() {
        IntConsumer consumer = System.out::println;
        IntConsumer consumer1 = consumer.andThen(value -> System.out.println(value*2));
        IntStream.of(1,2,3).forEach(consumer1);
    }

    /**
     * function用于映射
     */
    @Test
    public void functionTest() {
        //申明两个函数映射
        Function<Integer,Boolean> isEvenNum = (Integer a)->a%2==0;
        Function<Boolean,Boolean> negate = (Boolean a)->!a;

        //组合映射
        Function<Integer,Boolean> function = isEvenNum.andThen(negate);
        IntStream.of(1,2,3).mapToObj(Integer::valueOf).map(function).forEach(System.out::println);
    }
}
