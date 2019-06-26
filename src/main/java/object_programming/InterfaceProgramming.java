package object_programming;

/**
 * @author: penghuiping
 * @date: 2019/6/25 16:42
 * @description:
 */
public class InterfaceProgramming {

    public static void run(Run run) {
        run.runing();
    }

    public static void main(String[] args) {
        Run car = new Car();
        Run horse = new Horse();
        run(car);
        run(horse);
    }

}


interface Run {
    default void runing() {
        System.out.println("这是一个默认的跑动作");
    }
}



class Car implements Run{
    @Override
    public void runing() {
        System.out.println("汽车用四个轮子跑");
    }
}

class Horse implements Run {
    @Override
    public void runing() {
        System.out.println("马用四条腿来跑");
    }
}

