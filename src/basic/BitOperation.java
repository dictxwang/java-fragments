package basic;
import java.util.function.BiPredicate;
import java.util.function.IntFunction;
import java.util.function.IntPredicate;
// 注意：位运算优先级比算术运算优先级低
/**
 * 三种移位运算
 * >> 有符号位右移，连同符号位整体右移；正数高位补0，负数高位补1
 * >>> 无符号位右移，连同符号位整体右移，正数负数高位均用0补齐
 * << 左移，符号位不动，其他位左移（左移一位相当于乘以2）
 * 
 * 需要注意：负数是以补码形式存在的，在移位运算中也是以补码进行的
 * */
public class BitOperation {
     public static void main(String[] args) {
    	 /**原码、取反、补码*/
    	 System.out.println(15);
    	 System.out.println(~15);
    	 // 负数的补码等价于其对应整数取反再加1
    	 System.out.println((~15) + 1);
    	 // 对负数取反再加一，相当于求其绝对值
    	 System.out.println((~-15) + 1);
    	 
    	 /**
    	  * 取反和反码不一样：
    	  * 取反是连同符号位全部取反；
    	  * 反码：整数是其自身；负数是符号位不变，其余位取反。
    	  * */
    	 

    	 System.out.println("----------");
         /**移位 */
         // 左移位 -> 乘以2的n次幂
         System.out.println(3 << 2); // 12
         // 右移位(保留符号位) -> 除以2的n次幂
         System.out.println(4 >> 2); // 1
         // 负数右移符号位补1，正数补0
         // -1补码二进制全是1，右移后符号位补1仍然是1
         System.out.println(-1 >> 1); // -1
         System.out.println(-2 >> 1); // -1
         System.out.println(-4 >> 1); // -2
         // 无符号位右移(移位后符号位补0)
         System.out.println(-2 >>> 1); // 2147483647(max_int)
         System.out.println(4 >>> 1); // 2
         // 获取类型最大值最小值
         System.out.println(1 << 31); // -2147483648
         System.out.println(1 << -1); // -2147483648
         System.out.println((1 << 31) - 1); // 2147483647
         System.out.println(~(1 << 31)); // 2147483647
         // -2的补码除了最低位是0，其余位均为1
         System.out.println(-2 >>>1); // 2147483647
           
         /**&^运算*/
         // 奇偶判断: 二进制最后一位是0即是偶数
         IntPredicate oddPredicate = x -> (x & 1) == 0;
         System.out.println(oddPredicate.test(2)); // true
         System.out.println(oddPredicate.test(-3)); // false
           
         // 交换两个数: 利用异或运算满足交换律的特性(参考异或加密)
         int a = 10;
         int b = 6;
         a = a ^ b; // 相当于在加密
         b = a ^ b; // 解密
         a = a ^ b;
         System.out.printf("%d,%d\n", a, b); // 6,10
           
         // 判断符号是否相同：利用异或运算相同为0的特性，符号位相同异或后符号位0，即整数
         BiPredicate<Integer, Integer> sameSign = (x, y) -> (x ^ y) >= 0;
         System.out.println(sameSign.test(1, 2)); // true
         System.out.println(sameSign.test(-1, 29)); // false
         System.out.println(sameSign.test(0, 0)); // true
           
         /**复合运算*/
         // 取绝对值:
         // n>>31获取符号位（0正数，-1负数）
         // 正数与0异或是它自身，再减去0依然是其自身（即绝对值）
         // 负数是对其补码进行取反再加一
         // -1的补码二进制位全是1，因此负数与-1做异或运算相当于连同符号位取反
         // 负数是以补码存储的，例如：-2的补码用8位表示是 1111110
         IntFunction<Integer> abs = n -> (n ^ (n >> 31)) - (n >> 31);
         System.out.println(abs.apply(-12)); // 12
         System.out.println(abs.apply(0)); // 0
         System.out.println(abs.apply(12)); // 12

         /**利用位运算判断包含关系（参考linux文件系统权限管理）*/
         int read = 4, write = 2, exec = 1;
         IntPredicate readPermit = p -> (p & read) > 0;
         System.out.println(readPermit.test(4)); // true
         System.out.println(readPermit.test(6)); // true
         System.out.println(readPermit.test(3)); // false
         IntPredicate writeOrExecPermit = p -> (p & write + exec) > 0;
         System.out.println(writeOrExecPermit.test(2)); // true
         System.out.println(writeOrExecPermit.test(3)); // true
     }
}