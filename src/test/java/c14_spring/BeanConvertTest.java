package c14_spring;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.support.DefaultConversionService;

import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * @author penghuiping
 * @date 2022/7/14 22:09
 */
@Slf4j
public class BeanConvertTest {

    @Test
    public void convertTest() {
        DefaultConversionService conversionService = new DefaultConversionService();
        conversionService.addConverter(new Converter<String, Date>() {
            @Override
            public Date convert(String source) {
                    LocalDate localDate =  LocalDate.parse(source,DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                    return Date.from(Instant.ofEpochMilli(localDate.toEpochDay()));
            }
        });
        Date date = conversionService.convert("2022-01-01", Date.class);
        System.out.println(date);
    }
}
