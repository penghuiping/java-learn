package c4_oop;

/**
 * @author: penghuiping
 * @date: 2019/6/27 10:04
 * @description:
 */
class Car implements Run{
    @Override
    public void runing() {
        System.out.println("汽车用四个轮子跑");
    }
}
