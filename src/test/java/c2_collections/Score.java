package c2_collections;

/**
 * @author: penghuiping
 * @date: 2019/6/27 10:41
 * @description:
 */
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
