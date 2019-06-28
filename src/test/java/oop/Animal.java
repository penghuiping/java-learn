package oop;

/**
 * @author: penghuiping
 * @date: 2019/6/27 10:07
 * @description: 动物父类
 */
public class Animal {
    int age;

    public Animal(int age) {
        this.age = age;
    }

    public void eat() {
        System.out.println("动物需要吃饭");
    }


}
