package c14_spring_core;

import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.junit.Test;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.List;

/**
 * @author penghuiping
 * @date 2022/7/14 22:06
 */
@Slf4j
public class BeanFactoryTest {

    @Test
    public void createTest() {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
        //BeanDefinition
        applicationContext.registerBeanDefinition("userDao",BeanDefinitionBuilder
                .rootBeanDefinition(UserDaoImpl.class)
                .getBeanDefinition());
        applicationContext.registerBeanDefinition("userService",BeanDefinitionBuilder
                .rootBeanDefinition(UserServiceImpl.class)
                .getBeanDefinition());
        var beanFactory = applicationContext.getDefaultListableBeanFactory();
        //BeanPostProcessor
        beanFactory.addBeanPostProcessor(new BeanPostProcessor() {
            @Override
            public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
                return getProxy(bean);
            }
        });
        applicationContext.refresh();
        UserService userService = applicationContext.getBean(UserService.class);
        var res = userService.findAll();
        log.info("result:{}",res);

    }

    private Object getProxy(Object bean) {
        ProxyFactory proxyFactory = new  ProxyFactory();
        var advice = new MethodInterceptor() {
            @Nullable
            @Override
            public Object invoke(@Nonnull MethodInvocation invocation) throws Throwable {
                long now = System.currentTimeMillis();
                String className = invocation.getThis().getClass().getSimpleName();
                String methodName = invocation.getMethod().getName();
                log.info("{}.{}方法开始", className, methodName);
                var res = invocation.proceed();
                log.info("{}.{}方法结束,耗时:{}ms", className, methodName, System.currentTimeMillis() - now);
                return res;
            }
        };
        proxyFactory.addAdvisor(new DefaultPointcutAdvisor(new AnnotationMatchingPointcut(null,TimeLog.class),advice ));
        proxyFactory.addAdvisor(new DefaultPointcutAdvisor(new AnnotationMatchingPointcut(TimeLog.class),advice ));
        proxyFactory.setTarget(bean);
        return proxyFactory.getProxy();
    }

    @Test
    public void createTest1() {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
        ClassPathBeanDefinitionScanner scanner = new ClassPathBeanDefinitionScanner(applicationContext);
        scanner.scan("c14_spring_core");
        applicationContext.getDefaultListableBeanFactory().addBeanPostProcessor(new BeanPostProcessor() {
            @Override
            public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
                return getProxy(bean);
            }
        });
        applicationContext.refresh();
        UserService userService = applicationContext.getBean(UserService.class);
        var res = userService.findAll();
        log.info("result:{}",res);
    }


}

interface UserService {
    List<User> findAll();
}

@TimeLog
@Service
class UserServiceImpl implements UserService{

    @Autowired
    private UserDao userDao;

    @Override
    public List<User> findAll() {
        return userDao.findAll();
    }
}

interface UserDao {
    List<User> findAll();
}


@Repository
class UserDaoImpl implements UserDao{


    @TimeLog
    @Override
    public List<User> findAll() {
        return List.of(new User("1","jack"),new User("2","mary"));
    }
}

record User(String id,String name){

}

@Target({ElementType.TYPE,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@interface TimeLog {

}
