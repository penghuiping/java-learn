package c14_spring_core;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.propertyeditors.CustomDateEditor;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author penghuiping
 * @date 2022/7/27 22:28
 */
@Slf4j
public class BeanWrapperTest {

    //BeanWrapper,CustomEditorConfigurer,PropertyEditorRegistrar

    @Test
    public void test() {
        User user = new User();
        BeanWrapper wrapper = new BeanWrapperImpl(user);
        wrapper.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd"), false));
        wrapper.setPropertyValue("id", "1");
        wrapper.setPropertyValue("name", "jack");
        wrapper.setPropertyValue("birthday", "2000-01-01");
        log.info("user:{}", user);
    }


    @Getter
    @Setter
    @ToString
    class User {
        String id;
        String name;
        Date birthday;
    }
}
