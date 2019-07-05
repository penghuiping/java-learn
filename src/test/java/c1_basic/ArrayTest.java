package c1_basic;

import org.junit.Test;

import java.util.Arrays;

/**
 * @author: penghuiping
 * @date: 2019/6/27 10:19
 * @description: 数组相关操作
 */
public class ArrayTest {

    @Test
    public void test() {
        var arr = new int[]{1, 2, 3, 4, 5, 6, 7};
        for (var i = 0; i < arr.length; i++) {
            System.out.print(i);
        }
        System.out.println();

        //取值
        int a = arr[0];
        System.out.println(a);


        //二维数组
        var arr1 = new int[3][3];
        arr1[0] = new int[]{1, 2, 3};
        arr1[1] = new int[]{3, 1, 2};
        arr1[2] = new int[]{11, 2, 34};

        for(int i=0;i<3;i++) {
            for (int j=0;j<3;j++) {
                System.out.print(arr1[i][j]+" ");
            }
            System.out.println();
        }
        System.out.println();

        //数组排序+二分查找
        var arr2 = new int[]{2, 1, 33, 41, 51, 6, 17};
        Arrays.sort(arr2);
        for(int i=0;i<arr2.length;i++) {
            System.out.print(arr2[i]+" ");
        }
        System.out.println();
        int bb  = Arrays.binarySearch(arr2,6);
        System.out.println(bb);
    }
}
