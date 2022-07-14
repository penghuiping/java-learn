package c14_spring;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.junit.Test;
import org.springframework.aop.framework.ProxyFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author penghuiping
 * @date 2022/7/14 22:20
 */
public class AopTest {

    public interface HttpPost {
        public void doPost();
    }


    public class HttpPostImpl implements HttpPost {
        @Override
        public void doPost() {
            System.out.println("do http post....");
        }
    }


    public ProxyFactory initProxyFactory() {
        ProxyFactory proxyFactory = new ProxyFactory();
        proxyFactory.addAdvice(new MethodInterceptor() {
            @Nullable
            @Override
            public Object invoke(@Nonnull MethodInvocation invocation) throws Throwable {
                System.out.println("before method invocation,first interceptor");
                Object res = invocation.proceed();
                System.out.println("after method invocation,first interceptor");
                return res;
            }
        });
        proxyFactory.addAdvice(new MethodInterceptor() {
            @Nullable
            @Override
            public Object invoke(@Nonnull MethodInvocation invocation) throws Throwable {
                System.out.println("before method invocation,second interceptor");
                Object res =  invocation.proceed();
                System.out.println("after method invocation,second interceptor");
                return res;
            }
        });
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


