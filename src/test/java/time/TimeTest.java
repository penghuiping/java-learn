package time;

import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author: penghuiping
 * @date: 2019/6/28 15:39
 * @description:
 */
public class TimeTest {

    @Test
    public void test() {
        LocalDateTime localDateTime0 = LocalDateTime.now();
        System.out.println(localDateTime0);

        System.out.println(localDateTime0.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        LocalDateTime localDateTime = LocalDateTime.parse("2018-10-19 10:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        System.out.println(localDateTime.toString());

        System.out.println(localDateTime0.isAfter(localDateTime));

        System.out.println(localDateTime0.plusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        System.out.println(localDateTime0.minusDays(27).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        LocalDate localDate0 = LocalDate.now();
        LocalDateTime start = localDate0.atStartOfDay();
        LocalDateTime end = localDate0.atTime(23, 59, 59, 999);
        System.out.println(start.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        System.out.println(end.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        LocalDate localDate1 = LocalDate.of(2019, 10, 1);
        LocalDateTime start1 = localDate1.atStartOfDay();
        LocalDateTime end1 = localDate1.atTime(23, 59, 59, 999);
        System.out.println(start1.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        System.out.println(end1.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
    }
}
