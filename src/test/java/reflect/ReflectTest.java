package reflect;

import oop.Person;
import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @author: penghuiping
 * @date: 2019/6/28 17:28
 * @description: 反射测试
 */
public class ReflectTest {

    @Test
    public void test() throws Exception {

        //通过反射的方式，加载person类
        var cls = Class.forName("oop.Person");

        //获取类下声明的方法
        Method[] methods = cls.getDeclaredMethods();
        for (Method method : methods) {
            System.out.println(method.getName());
        }

        System.out.println();

        //获取类下声明的属性
        Field[] fields = cls.getDeclaredFields();
        for (var field : fields) {
            System.out.println(field.getName());
        }

        //使用反射直接调用方法修改名字
        var p = new Person(27, "小明");
        p.desc();

        var method = cls.getDeclaredMethod("setName", String.class);
        method.invoke(p, "小张");
        p.desc();

    }
}
