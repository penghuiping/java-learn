package lock;

import org.junit.Test;
import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * @author: penghuiping
 * @date: 2019/7/5 13:24
 * @description:
 */
public class UnsafeTest {


    private static final Unsafe unsafe = createUnsafe();

    private volatile int count = 0;

    private static final long VALUE = unsafe.objectFieldOffset(getValueField());

    public static Field  getValueField() {
        try {
            return OptimisticLock.class.getDeclaredField("count");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            return null;
        }
    }



    public static Unsafe createUnsafe() {
        try {
            Class<?> unsafeClass = Class.forName("sun.misc.Unsafe");
            Field field = unsafeClass.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            Unsafe unsafe = (Unsafe) field.get(null);
            return unsafe;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }





    @Test
    public void test() {
        System.out.println(unsafe.pageSize());

        System.out.println(VALUE);
    }
}
