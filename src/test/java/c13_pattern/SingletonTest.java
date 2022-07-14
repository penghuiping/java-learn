package c13_pattern;

import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 单例
 *
 * @author penghuiping
 * @date 2022/2/11 22:11
 */
public class SingletonTest {

    @Test
    public void test() {
        DateManager dateManager1 = DateManager.getInstance();
        DateManager dateManager2 = DateManager.getInstance();
        System.out.println(dateManager1.toString());
        System.out.println(dateManager2.toString());
    }

}

class DateManager {
    private static volatile DateManager instance;

    private DateManager() {

    }

    public static DateManager getInstance() {
        if (null == instance) {
            synchronized (DateManager.class) {
                if (null == instance) {
                    instance = new DateManager();
                }
            }
        }
        return instance;
    }


    public Date fromLocalDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay().toInstant(ZoneOffset.ofHours(8)));
    }

    public Date fromLocalDateTime(LocalDateTime localDateTime) {
        return Date.from(localDateTime.toInstant(ZoneOffset.ofHours(8)));
    }
}
