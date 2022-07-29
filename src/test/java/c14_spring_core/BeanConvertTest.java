package c14_spring_core;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.support.DefaultFormattingConversionService;

import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author penghuiping
 * @date 2022/7/14 22:09
 */
@Slf4j
public class BeanConvertTest {

    @Test
    public void convertTest() {
        DefaultFormattingConversionService conversionService = new DefaultFormattingConversionService();
        conversionService.addConverter(new Converter<String, Date>() {
            @Override
            public Date convert(String source) {
                LocalDate localDate = LocalDate.parse(source, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                return Date.from(Instant.ofEpochMilli(localDate.toEpochDay()));
            }
        });

        Date date = conversionService.convert("2022-01-01", Date.class);
        System.out.println(date);
    }

    @Test
    public void covertTest2() {
        DefaultFormattingConversionService conversionService = new DefaultFormattingConversionService();
        conversionService.addConverter(new Converter<String, List<Integer>>() {

            @Override
            public List<Integer> convert(String source) {
                if ('[' == source.charAt(0) && ']' == source.charAt(source.length() - 1)) {
                    String source0 = source.substring(1, source.length() - 1);
                    return Arrays.stream(source0.split(",")).map(Integer::valueOf).collect(Collectors.toList());
                }
                return null;
            }
        });

        String target = "[1,2,3,4,5]";
        List<Integer> res = (List<Integer>) conversionService
                .convert(target, TypeDescriptor.collection(List.class, TypeDescriptor.valueOf(Integer.class)));
        System.out.println(res);
    }
}
