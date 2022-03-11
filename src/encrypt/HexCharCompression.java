package encrypt;

import java.util.*;

/**
 * Created by wangqiang on 2018/11/16.
 *
 * 一种自创的16进制字符串（常见如uuid字符串）压缩算法
 * 基本原理： 将16进制的字符串，转换成64进制的字符串（0-9A-Za-z_-）
 *  原16进制字符（通过模拟4个二进制位表示）的每3个字符拆分成64进制字符（通过模拟6个二进制位表示）的2个字符表示，从而实现压缩的目的
 *  能节省大约30%的存储
 */
public class HexCharCompression {

    // 十六进制字符串
    private static final char[] HEX_CHARS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    // 十六进制的2进制串
    private static final String[] HEX_BINARY_ARRAY = {"0000", "0001", "0010", "0011", "0100", "0101", "0110", "0111", "1000", "1001", "1010", "1011", "1100", "1101", "1110", "1111"};
    // 64位映射表
    private static final char[] CHAR64STR = new char[64];
    // 64位映射表的位置关系
    private static Map<String, Integer> char64StrMap;
    // 1个半字节二进制串
    private static Map<String, Integer> oneHalfByteBinaryMap;
    // 1个半字节二进制串反向映射
    private static Map<Integer, String> oneHalfByteBinaryMirrorMap;
    private static final Random random = new Random();

    static {
        for (int i = 0; i < 10; i++) {
            CHAR64STR[i] = String.valueOf(i).charAt(0);
        }
        for (int j = 65; j <= 90; j++) {
            CHAR64STR[10 + j - 65] = (char)j;
        }
        for (int k = 97; k <= 122; k++) {
            CHAR64STR[36 + k - 97] = (char)k;
        }
        CHAR64STR[62] = '_';
        CHAR64STR[63] = '-';
    }

    /***
     * 解压缩用户id
     * @param zipUid
     * @return
     */
    public static String unzipLoginUid(String zipUid) {
        if (zipUid == null || zipUid.isEmpty() || zipUid.length() != 22) {
            return null;
        }
        initChar64StrMap();
        initOneHalfBinaries();
        StringBuilder hexSb = new StringBuilder();
        for (int i = 0; i < 22; i++) {
            hexSb.append(oneHalfByteBinaryMirrorMap.get(char64StrMap.get(zipUid.substring(i, i + 1))));
        }
        // 每4位转换成一个16进制字符
        StringBuilder result = new StringBuilder();
        // 每六个字符算作一个半字节
        for (int j = 0;;) {
            result.append(HEX_CHARS[Integer.valueOf(hexSb.substring(j, j + 4), 2)]);
            j = j + 4;
            if (j >= 132) {
                break;
            }
        }

        return result.substring(1, 33);
    }

    /***
     * 对用户id进行压缩
     * @param uid
     * @return
     */
    public static String zipLoginUid(String uid) {
        if (uid == null || uid.isEmpty() || uid.length() != 32) {
            return null;
        }
        initChar64StrMap();
        initOneHalfBinaries();
        // 先补齐33位
        uid = String.valueOf(random.nextInt(10)) + uid;
        StringBuilder hexSb = new StringBuilder();
        for (int i = 0; i < 33; i++) {
            hexSb.append(HEX_BINARY_ARRAY[Integer.valueOf(uid.substring(i, i+1), 16)]);
        }
        StringBuilder result = new StringBuilder();
        // 每六个字符算作一个半字节
        for (int j = 0;;) {
            result.append(CHAR64STR[oneHalfByteBinaryMap.get(hexSb.substring(j, j + 6))]);
            j = j + 6;
            if (j >= 132) {
                break;
            }
        }

        return result.toString();
    }

    private static void initOneHalfBinaries() {
        if (oneHalfByteBinaryMap == null) {
            synchronized (HexCharCompression.class) {
                if (oneHalfByteBinaryMap == null) {
                    oneHalfByteBinaryMap = generateOneHalfHexBinaryMap();
                }
                if (oneHalfByteBinaryMirrorMap != null) {
                    oneHalfByteBinaryMirrorMap.clear();
                } else {
                    oneHalfByteBinaryMirrorMap = new HashMap<>();
                }
                for (String k : oneHalfByteBinaryMap.keySet()) {
                    oneHalfByteBinaryMirrorMap.put(oneHalfByteBinaryMap.get(k), k);
                }
            }
        }
    }

    private static void initChar64StrMap() {
        if (char64StrMap == null) {
            synchronized (HexCharCompression.class) {
                if (char64StrMap == null) {
                    char64StrMap = new HashMap<>();
                    for (int i = 0; i < CHAR64STR.length; i++) {
                        char64StrMap.put(String.valueOf(CHAR64STR[i]), i);
                    }
                }
            }
        }
    }

    /**
     * 生成使用1个半字节标识二进制的全排列
     * @return
     */
    private static Map<String, Integer> generateOneHalfHexBinaryMap() {
        Map<String, Integer> result = new HashMap<>();
        Set<String> hexSet = new TreeSet<>();
        int index = 0;
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 16; j++) {
                for (int k = 0; k < 16; k++) {
                    String hex = HEX_BINARY_ARRAY[i] + HEX_BINARY_ARRAY[j] + HEX_BINARY_ARRAY[k];
                    hexSet.add(hex.substring(0, 6));
                    hexSet.add(hex.substring(6, 12));
                }
            }
        }
        for (String hex : hexSet) {
            result.put(hex, index++);
        }
        return result;
    }

    public static void main(String[] args) {
        String uid = "6e998906241a4aca83be9a738d6416b7";

        String zip = zipLoginUid(uid);
        System.out.println(zip);

        String unzip = unzipLoginUid(zip);
        System.out.println(unzip);  // DkcOa691fAoeE_cdEDP1Qt
        System.out.println(uid.equals(unzip));
    }
}
