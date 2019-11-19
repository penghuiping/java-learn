package c12_serialize;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @author penghuiping
 * @date 2019/11/19 15:20
 */
public class JsonTest {


    @Test
    public void test1() throws Exception{
        List<A> dataList = new ArrayList<A>();
        dataList.add(new C("10", 5));
        dataList.add(new B(8, 5));
        D d = new D();
        d.setList(dataList);
        ObjectMapper mapper = new ObjectMapper();
        String data = mapper.writeValueAsString(d);
        System.out.println(data);
        D result = mapper.readValue(data, D.class);

        System.out.println(result.getList());
    }

    public static class D {
        List<A> list;

        public List<A> getList() {
            return list;
        }

        public void setList(List<A> list) {
            this.list = list;
        }

    }

    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "typeName")
    @JsonSubTypes({ @JsonSubTypes.Type(value = B.class, name = "B"),
            @JsonSubTypes.Type(value = C.class, name = "C") })
    public static class A {
        protected int a;

        public A() {
        }

        public A(int a) {
            this.a = a;
        }

        public int getA() {
            return a;
        }

        public void setA(int a) {
            this.a = a;
        }

    }

    @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "typeName")
    public static class B extends A {
        public B() {
        }

        public B(int b, int a) {
            super(a);
        }

        private int b;

        public int getB() {
            return b;
        }

        public void setB(int b) {
            this.b = b;
        }
    }

    @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "typeName")
    public static class C extends A {
        private String c;

        public C() {
        }

        public C(String c, int a) {
            super(a);
            this.c = c;
        }

        public String getC() {
            return c;
        }

        public void setC(String c) {
            this.c = c;
        }

    }
}
