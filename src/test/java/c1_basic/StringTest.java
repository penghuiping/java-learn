package c1_basic;

import org.junit.Test;

import java.nio.charset.Charset;
import java.util.regex.Pattern;

/**
 * @author: penghuiping
 * @date: 2019/6/28 15:46
 * @description: 字符串相关操作
 */
public class StringTest {

    @Test
    public void test() {
        var a = "hello world";

        //判断是否包含
        if (a.contains("hello")) {
            System.out.println("包含hello");
        }

        if (a.indexOf("hello") >= 0) {
            System.out.println("包含hello");
        }

        //截取字符串
        var b = a.substring(0, 5);
        System.out.println(b);

        //string to int
        var c = "13";
        var c1 = Integer.parseInt(c);


        //int to string
        var c2 = 13 + "";

        //拼接字符串
        var d = "hello" + " world" + "!!!";
        System.out.println(d);

        //更有效率的拼接字符串
        StringBuilder sb = new StringBuilder();
        sb.append("hello");
        sb.append(" ");
        sb.append("world");
        sb.append("!!!");
        System.out.println(sb);

        //把utf-8编码转换成GBK编码
        var bb = "你好，世界";
        byte[] arr = bb.getBytes(Charset.forName("utf-8"));
        var bb1 = new String(arr, Charset.forName("GBK"));
        System.out.println(bb1);


        //使用regex截取字符串
        var cc = "我是一段文字<span>中国你好</span>！！！！你知道吗？<span>很好</span>就是这样子。";
        var pattern = Pattern.compile("<span>([^<]*)</span>");
        var matcher = pattern.matcher(cc);
        System.out.println("分组数量:"+matcher.groupCount());
        matcher.results().forEach(matchResult -> {
            var tmp = matchResult.group(1);
            System.out.println(tmp);
        });

    }
}
