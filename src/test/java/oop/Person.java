package oop;

/**
 * @author: penghuiping
 * @date: 2019/6/27 10:10
 * @description: 人子类
 */
public class Person extends Animal {
    private String name;

    public Person(int age, String name) {
        super(age);
        this.name = name;
    }

    public void desc() {
        System.out.println("人的名字是:" + name + ",年龄是:" + age);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void eat() {
        System.out.println("人吃米饭");
    }
}
