package c15_spring_cloud;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

/**
 * @author penghuiping
 * @date 2022/7/30 11:25
 */
@Slf4j
public class SpringBootTest {

    @Test
    public void test() {
        SpringApplication springApplication = new SpringApplication();
        springApplication.addInitializers(new ApplicationContextInitializer<ConfigurableApplicationContext>() {
            @Override
            public void initialize(ConfigurableApplicationContext applicationContext) {
            }
        });
        springApplication.addPrimarySources(Lists.newArrayList(SpringBootTest.class));
        springApplication.run();

    }


    @Bean
    ApplicationRunner applicationRunner(ApplicationContext applicationContext) {
        return new ApplicationRunner() {
            @Override
            public void run(ApplicationArguments args) throws Exception {
                log.info("你好，世界");
                var event = new MyApplicationEvent("这是一个自定义事件");
                event.setApplicationContext(applicationContext);
                applicationContext.publishEvent(event);
            }
        };
    }

    @Bean
    ApplicationListener<MyApplicationEvent> applicationEventApplicationListener() {
        return new ApplicationListener<MyApplicationEvent>() {
            @Override
            public void onApplicationEvent(MyApplicationEvent event) {
                log.info("event:{}", event.getSource());
                event.printAllBeanNamesInSpringContainer();
            }
        };
    }

    class MyApplicationEvent extends ApplicationEvent implements ApplicationContextAware {
        private ApplicationContext applicationContext;

        public MyApplicationEvent(Object source) {
            super(source);
        }

        @Override
        public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
            this.applicationContext = applicationContext;
        }


        public void printAllBeanNamesInSpringContainer() {
            var listBeanFactory = (ListableBeanFactory) applicationContext.getAutowireCapableBeanFactory();
            var beanDefinitionNames = listBeanFactory.getBeanDefinitionNames();
            for (String beanName : beanDefinitionNames) {
                log.info("beanName:{}", beanName);
            }
        }
    }
}
