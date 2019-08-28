package c1_basic;

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

        //有符号左移
        int bit1 = 1 << 3;
        System.out.println("有符号左移运算:" + bit1);

        //有符号右移
        int bit2 = 8 >> 3;
        System.out.println("有符号右移运算:" + bit2);

        //无符号右移动
        int bit8 = -8 >>> 3;
        System.out.println("-8二进制为:" + Integer.toBinaryString(-8));
        System.out.println("无符号右移运算:" + Integer.toBinaryString(bit8));

        //-1 二进制表示 负数在计算机中都是用补码表示的,补码是原码取反加一
        int bit7 = -1;
        System.out.println("-1的二进制表示为:" + Integer.toBinaryString(bit7));

        //如果被除数是偶数，&运算可以用来代替%(取余)
        int bit9 = 121;
        System.out.println("操作符&与%相同:"+((bit9%8)==(bit9&(8-1))));
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
        } while (a > 0);
        System.out.println();
    }
}
