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

        //位运算
        int bit = 0b01001110 & 0x0F;
        System.out.println("位运算-与:"+Integer.toBinaryString(bit));

        int bit3 = 0b01001110 | 0x0F;
        System.out.println("位运算-或:"+Integer.toBinaryString(bit3));


        int bit1 = 1<<3;
        System.out.println("左移运算:"+bit1);

        int bit2 = 8>>3;
        System.out.println("右移运算:"+bit2);

        //for 循环
        for (int i = 0; i < 10; i++) {
            System.out.print(i);
        }
        System.out.println();

        //while 循环
        int a = 10;
        while (a > 0) {
            System.out.print(a);
            a--;
        }
        System.out.println();

        //do while循环
        a = 10;
        do {
            System.out.print(a);
            a--;
        }while (a>0);
        System.out.println();

        //函数与方法
        print("hello world");

    }



    public static void print(Object c) {
        System.out.println(c);
    }
}
