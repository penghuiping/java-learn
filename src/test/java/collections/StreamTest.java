package collections;

import org.junit.Test;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author: penghuiping
 * @date: 2019/6/27 10:41
 * @description:
 */
public class StreamTest {

    @Test
    public void test() {
        var list = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9);

        //求和
        var result = list.stream().reduce(0, (a, b) -> a + b);
        System.out.println(result);

        //求平均数
        var result1 = list.stream().collect(Collectors.averagingInt(value -> value));
        System.out.println(result1);

        //求最大数
        var result3 = list.stream().max((o1, o2) -> o1 - o2);
        System.out.println(result3.get());

        //求最小数
        var result4 = list.stream().min((o1, o2) -> o1 - o2);
        System.out.println(result4.get());

        //分组
        Score score0 = new Score("Math", 90, "jack");
        Score score1 = new Score("Chinese", 72, "jack");
        Score score2 = new Score("English", 60, "jack");

        Score score3 = new Score("Math", 89, "mary");
        Score score4 = new Score("Chinese", 52, "mary");
        Score score5 = new Score("English", 80, "mary");

        var scores = List.of(score0, score1, score2, score3, score4, score5);

        //根据姓名分组,并且计算分数相关统计指标
        var result2 = scores.stream().collect(Collectors.groupingBy(Score::getName, Collectors.summarizingInt(value -> value.getScore())));
        System.out.println(result2);
    }
}
