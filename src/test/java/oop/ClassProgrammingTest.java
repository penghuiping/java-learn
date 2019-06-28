package oop;

import org.junit.Test;

/**
 * @author: penghuiping
 * @date: 2019/6/27 10:12
 * @description: 类对象编程
 */
public class ClassProgrammingTest {

    public static void eat(Animal animal) {
        animal.eat();
    }


    @Test
    public void test() {
        Animal animal1 = new Person(12, "jack");
        Animal animal2 = new Monkey(5);

        ((Person) animal1).desc();

        eat(animal1);
        eat(animal2);
    }
}
