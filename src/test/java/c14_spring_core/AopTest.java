package c14_spring_core;

import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.junit.Test;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.JdkRegexpMethodPointcut;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author penghuiping
 * @date 2022/7/14 22:20
 */
@Slf4j
public class AopTest {

    public interface HttpPost {
        public void doPost();
    }


    public class HttpPostImpl implements HttpPost {
        @Override
        public void doPost() {
            log.info("do http post....");
        }
    }


    public ProxyFactory initProxyFactory() {
        ProxyFactory proxyFactory = new ProxyFactory();
        proxyFactory.addAdvice(new MethodInterceptor() {
            @Nullable
            @Override
            public Object invoke(@Nonnull MethodInvocation invocation) throws Throwable {
                log.info("before method invocation,first interceptor");
                Object res = invocation.proceed();
                log.info("after method invocation,first interceptor");
                return res;
            }
        });
        JdkRegexpMethodPointcut jdkRegexpMethodPointcut = new JdkRegexpMethodPointcut();
        jdkRegexpMethodPointcut.setPattern(".*doPost.*");
        proxyFactory.addAdvisor(new DefaultPointcutAdvisor(jdkRegexpMethodPointcut, new MethodInterceptor() {
            @Nullable
            @Override
            public Object invoke(@Nonnull MethodInvocation invocation) throws Throwable {
                log.info("before method invocation,second interceptor");
                Object res = invocation.proceed();
                log.info("after method invocation,second interceptor");
                return res;
            }
        }));
        proxyFactory.addInterface(HttpPost.class);
        proxyFactory.setTarget(new HttpPostImpl());
        return proxyFactory;
    }

    @Test
    public void test() {
        HttpPost httpPost = (HttpPost)initProxyFactory().getProxy();
        httpPost.doPost();
    }
}


