package c13_pattern;

import org.junit.Test;

import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

/**
 * 动态代理
 *
 * @author penghuiping
 * @date 2022/2/11 22:11
 */
public class ProxyTest {

    @Test
    public void jdkTest() {
        List<String> origin = new ArrayList<>();
        List<String> proxy = ListProxy.getProxy(origin);
        for(int i=0;i<10;i++) {
            proxy.add("hello world"+i);
        }
        proxy.size();
    }
}


class ListProxy {
    public static List getProxy(List origin) {
        List list = (List) Proxy.newProxyInstance(ProxyTest.class.getClassLoader(),
                new Class[]{List.class},
                (proxy, method, args) -> {
                    long start = System.currentTimeMillis();
                    System.out.printf("开始执行:%s%n", method.getName());
                    Object result = method.invoke(origin, args);
                    System.out.printf("结束执行:%s,总耗时%dms%n",method.getName(),System.currentTimeMillis()-start);
                    return result;
                });
        return list;
    }
}
