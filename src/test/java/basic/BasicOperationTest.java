package basic;

import org.junit.Test;

/**
 * @author: penghuiping
 * @date: 2019/6/26 18:01
 * @description:
 */
public class BasicOperationTest {


    /**
     * 8大基本数据类型
     */
    @Test
    public void basicDataType() {
        byte b1 = 1;
        short b2 = 2;
        char b3 = 3;
        int b4 = 4;
        long b5 = 5;
        float b6 = 1.1f;
        double b7 = 1.2d;
        boolean b8 = false;
    }


    /**
     * 加减乘除，取模
     */
    @Test
    public void basicOperation() {
        int c = 1 + 1;
        c = 1 - 1;
        c = 1 * 2;
        c = 1 / 2;
        c = 1 % 2;
    }


    /**
     * 位运算
     */
    @Test
    public void bitOperation() {
        //位运算

        //与运算
        int bit = 0b01001110 & 0x0F;
        System.out.println("位运算-与:" + Integer.toBinaryString(bit));

        //或运算
        int bit3 = 0b01001110 | 0x0F;
        System.out.println("位运算-或:" + Integer.toBinaryString(bit3));

        //取反运算
        int bit4 = ~0b01001111;
        System.out.println("位运算-取反:" + Integer.toBinaryString(bit4));

        //异或运算
        int bit5 = 0b01001110 ^ 0x0;
        System.out.println("位运算-异或:" + Integer.toBinaryString(bit5));

        //左移
        int bit1 = 1 << 3;
        System.out.println("左移运算:" + bit1);

        //右移
        int bit2 = 8 >> 3;
        System.out.println("右移运算:" + bit2);
    }

    /**
     * for 循环
     */
    @Test
    public void forTest() {
        //for 循环
        for (int i = 0; i < 10; i++) {
            System.out.print(i);
        }
        System.out.println();
    }

    /**
     * while 循环
     */
    @Test
    public void whileTest() {
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
    }
}
