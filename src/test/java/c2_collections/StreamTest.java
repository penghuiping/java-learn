package c2_collections;

import org.junit.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.security.cert.CollectionCertStoreParameters;
import java.util.Arrays;
import java.util.List;
import java.util.function.IntSupplier;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * @author: penghuiping
 * @date: 2019/6/27 10:41
 * @description:
 */
public class StreamTest {

    @Test
    public void test() {
        var list = List.of(1, 2, 3, 4, 5, 6, 7, 8,8, 9);

        //求和
        var result = list.stream().reduce(0, (a, b) -> a + b);
        System.out.format("求和:%s\n",result);

        //求平均数
        var result1 = list.stream().collect(Collectors.averagingInt(value -> value));
        System.out.format("求平均数:%s\n",result1);

        //求最大数
        var result3 = list.stream().max((o1, o2) -> o1 - o2);
        System.out.format("求最大值:%s\n",result3.get());

        //求最小数
        var result4 = list.stream().min((o1, o2) -> o1 - o2);
        System.out.format("求最小值:%s\n",result4.get());

        //获取元素中的前3个
        var result5 = list.stream().limit(3).collect(Collectors.toList());
        System.out.format("取集合元素的前三个:%s\n",result5);

        //获取3至6范围的元素
        var result6 = list.stream().skip(3).limit(3).collect(Collectors.toList());
        System.out.format("取索引3至6范围的元素:%s\n",result6);

        //去重复元素
        var result7 = list.stream().distinct().collect(Collectors.toList());
        System.out.format("去重复元素:%s\n",result7);

        //倒叙排序
        var result8 = list.stream().sorted((o1, o2) -> o2-o1).collect(Collectors.toList());
        System.out.format("倒叙排序:%s\n",result8);

        //分组
        Score score0 = new Score("Math", 90, "jack");
        Score score1 = new Score("Chinese", 72, "jack");
        Score score2 = new Score("English", 60, "jack");

        Score score3 = new Score("Math", 89, "mary");
        Score score4 = new Score("Chinese", 52, "mary");
        Score score5 = new Score("English", 80, "mary");

        var scores = List.of(score0, score1, score2, score3, score4, score5);

        //根据姓名分组,并且计算分数相关统计指标
        var result2 = scores.stream()
                .collect(Collectors.groupingBy(Score::getName, Collectors.summarizingInt(Score::getScore)));
        System.out.println(result2);
    }

    @Test
    public void mapAndFlatMapTest() {
        var list = List.of("hello","world");
        list.stream().map(String::length).forEach(System.out::println);
        list.stream().map(s->s.split("")).flatMap(Arrays::stream).forEach(System.out::println);
    }

    @Test
    public void buildStreamTest() {
        //stream from values
        Stream.of(1,2,3,4,5).forEach(System.out::print);
        System.out.println();
        //stream from arrays
        int[] values = new int[]{1,2,3,4,5};
        Arrays.stream(values).forEach(System.out::print);
        System.out.println();
        //stream from functions
        Stream.iterate(0,value -> value+1).limit(3).forEach(System.out::print);
        System.out.println();
        Stream.iterate(0,value -> value+1).takeWhile(a->a<100).map(a->a+" ").forEach(System.out::print);
        System.out.println();
        IntStream.generate(new IntSupplier() {
            private int value=0;
            @Override
            public int getAsInt() {
                return this.value++;
            }
        }).takeWhile(a->a<100).mapToObj(a->a+" ").forEach(System.out::print);
        System.out.println();
        Stream.generate(Math::random).limit(3).map(a->a+" ").forEach(System.out::print);
    }

    /**
     * 对于大数据数据源操作，使用parallel stream可以提高效率,因为用了多核
     * parallel stream底层用fork-join框架,所以在操作的时候需要注意并发问题
     */
    @Test
    public void testParallelStream() {
        long start = System.currentTimeMillis();
        long sum = 0L;
        for(long i=0L;i<100000000000L;i++) {
            sum = sum+i;
        }
        System.out.println(sum);
        System.out.println("循环耗时为："+(System.currentTimeMillis()-start)+"ms");

        long start1 = System.currentTimeMillis();
        long sum1 = LongStream.range(0,100000000000L).parallel()
                .reduce(0L,Long::sum);
        System.out.println(sum1);
        System.out.println("parallelStream耗时为："+(System.currentTimeMillis()-start1)+"ms");
    }
}
