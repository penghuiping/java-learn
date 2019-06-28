package oop;

/**
 * @author: penghuiping
 * @date: 2019/6/27 10:08
 * @description: 猴子子类
 */
class Monkey extends Animal {

    public Monkey(int age) {
        super(age);
    }

    @Override
    public void eat() {
        System.out.println("猴子吃香蕉");
    }
}
