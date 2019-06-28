package oop;

/**
 * @author: penghuiping
 * @date: 2019/6/27 10:05
 * @description:
 */
interface Run {
    default void runing() {
        System.out.println("这是一个默认的跑动作");
    }
}
