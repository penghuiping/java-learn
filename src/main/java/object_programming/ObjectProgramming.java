package object_programming;

/**
 * @author: penghuiping
 * @date: 2019/6/25 16:30
 * @description:
 */
public class ObjectProgramming {



    public static void eat(Animal animal) {
        animal.eat();
    }


    public static void main(String[] args) {
        Animal animal1 = new Person(12,"jack");
        Animal animal2 = new Monkey(5);

        ((Person) animal1).desc();

        eat(animal1);
        eat(animal2);
    }

}

//动物父类
class Animal {
    int age;

    public Animal(int age) {
        this.age = age;
    }

    public void eat() {
        System.out.println("动物需要吃饭");
    }


}

//人子类
class Person extends Animal {
    String name;

    public Person(int age, String name) {
        super(age);
        this.name = name;
    }

    public void desc() {
        System.out.println("人的名字是:" + name + ",年龄是:" + age);
    }

    @Override
    public void eat() {
        System.out.println("人吃米饭");
    }
}


//猴子子类
class Monkey extends Animal {

    public Monkey(int age) {
        super(age);
    }

    @Override
    public void eat() {
        System.out.println("猴子吃香蕉");
    }
}

