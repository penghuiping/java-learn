package collections;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author: penghuiping
 * @date: 2019/6/26 14:12
 * @description:
 */
public class LambdaExamples {

    public static void main(String[] args) {
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
        Score score0 = new Score("Math",90,"jack");
        Score score1 = new Score("Chinese",72,"jack");
        Score score2 = new Score("English",60,"jack");

        Score score3 = new Score("Math",89,"mary");
        Score score4 = new Score("Chinese",52,"mary");
        Score score5 = new Score("English",80,"mary");

        var scores = List.of(score0,score1,score2,score3,score4,score5);

        //根据姓名分组,并且计算分数相关统计指标
        var result2 = scores.stream().collect(Collectors.groupingBy(Score::getName,Collectors.summarizingInt(value -> value.getScore())));
        System.out.println(result2);


    }
}

class Score {

    /*课程名称*/
    private String course;

    /*分数*/
    private int score;

    /*姓名*/
    private String name;

    public Score(String course, int score, String name) {
        this.course = course;
        this.score = score;
        this.name = name;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
