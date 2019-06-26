package basic;

/**
 * @author: penghuiping
 * @date: 2019/6/25 16:20
 * @description:
 */
public class BasicOperation {

    public static void main(String[] args) {
        //8大基本数据类型

        byte b1 = 1;
        short b2 = 2;
        char b3 = 3;
        int b4 = 4;
        long b5 = 5;
        float b6 = 1.1f;
        double b7 = 1.2d;
        boolean b8 = false;


        //加减乘除，取模
        int c = 1 + 1;
        c = 1 - 1;
        c = 1 * 2;
        c = 1 / 2;
        c = 1 % 2;


        //for 循环
        for (int i = 0; i < 10; i++) {
            System.out.println(i);
        }

        //while 循环
        int a = 10;
        while (a > 0) {
            System.out.println(a);
            a--;
        }

        //do while循环
        a = 10;
        do {
            System.out.println(a);
            a--;
        }while (a>0);

        //函数与方法
        print("hello world");

    }



    public static void print(Object c) {
        System.out.println(c);
    }
}
