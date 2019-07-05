package c4_oop;

import org.junit.Test;

/**
 * @author: penghuiping
 * @date: 2019/6/27 10:12
 * @description: 面向接口编程
 */
public class InterfaceProgrammingTest {

    public static void run(Run run) {
        run.runing();
    }

    /**
     * Car与Horse类都实现了Run接口
     * <p>
     * run函数入参是Run接口，通过多态技术，实现了传入不同的实例对象，调用的就是不同实例对象的running方法
     */
    @Test
    public void test() {
        Run car = new Car();
        Run horse = new Horse();
        run(car);
        run(horse);
    }
}
