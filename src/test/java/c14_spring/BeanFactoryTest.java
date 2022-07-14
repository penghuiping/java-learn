package c14_spring;

import org.junit.Test;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;

import java.util.List;

/**
 * @author penghuiping
 * @date 2022/7/14 22:06
 */
public class BeanFactoryTest {

    @Test
    public void createTest() {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        beanFactory.registerSingleton("myList", List.of("a","b"));
        List obj = (List)beanFactory.getBean("myList");
        System.out.println(obj);
    }
}
