package encrypt;

public class HexUtil {

    private static final char[] digits = new char[]{'0', '1', '2', '3', '4',//
            '5', '6', '7', '8', '9',//
            'A', 'B', 'C', 'D', 'E',//
            'F'};
    private static final char[] HEX = "0123456789ABCDEF".toCharArray();
    private static final byte[] EMPTY_BYTES = new byte[0];
    private static final int[] CONVERT = new int[128];

    static {
        for (int i = 0; i < 128; i++) {
            CONVERT[i] = 0;
        }
        for (int i = '0'; i <= '9'; i++) {
            CONVERT[i] = i - '0';
        }

        for (int i = 'a'; i <= 'z'; i++) {
            CONVERT[i] = i - 'a' + 10;
        }

        for (int i = 'A'; i <= 'Z'; i++) {
            CONVERT[i] = i - 'A' + 10;
        }
    }

    /**
     * 将单个字节转成Hex String
     *
     * @param b 字节
     * @return String Hex String
     */
    public static String dump(byte b) {
        char[] buf = new char[2];
        buf[1] = HEX[b & 0xF];
        b = (byte) (b >>> 4);
        buf[0] = HEX[b & 0xF];
        return new String(buf);
    }

    /**
     * 将字节数组转成Hex String
     *
     * @param bytes
     * @return
     */
    public static String dump(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        final int len = bytes.length;
        final int sz = bytes.length << 1;
        char[] buf = new char[sz];
        for (int i = 0; i < len; i++) {
            byte b = bytes[i];
            int j = i << 1;
            buf[j + 1] = HEX[b & 0xF];
            b = (byte) (b >>> 4);
            buf[j] = HEX[b & 0xF];
        }
        return new String(buf);
    }

    private static int from(char ch) {
        return ch > 128 ? 0 : CONVERT[ch];
    }

    public static byte[] from(String str) {
        if (str == null || str.length() == 0) {
            return EMPTY_BYTES;
        }
        byte[] bytes = new byte[str.length() / 2];
        for (int i = 0; i < bytes.length; i++) {
            char high = str.charAt(i * 2);
            char low = str.charAt(i * 2 + 1);
            bytes[i] = (byte) ((from(high) << 4) + from(low));
        }
        return bytes;
    }

    public static byte[] hexStr2Bytes(String str) {
        if (str == null || str.equals("")) {
            return EMPTY_BYTES;
        }

        byte[] bytes = new byte[str.length() / 2];
        for (int i = 0; i < bytes.length; i++) {
            char high = str.charAt(i * 2);
            char low = str.charAt(i * 2 + 1);
            bytes[i] = (byte) (char2Byte(high) * 16 + char2Byte(low));
        }
        return bytes;
    }

    /**
     * 字符到字节
     *
     * @param ch
     * @return byte
     */
    public static byte char2Byte(char ch) {
        if (ch >= '0' && ch <= '9') {
            return (byte) (ch - '0');
        } else if (ch >= 'a' && ch <= 'f') {
            return (byte) (ch - 'a' + 10);
        } else if (ch >= 'A' && ch <= 'F') {
            return (byte) (ch - 'A' + 10);
        } else {
            return 0;
        }
    }

    /**
     * 将字节数组转成Hex String
     */
    public static String bytes2HexStr(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        return bytes2HexStr(bytes, 0, bytes.length);
    }

    /**
     * 字节转16进制编码字符
     */
    public static String bytes2HexStr(byte[] bytes, int offset, int length) {
        if (bytes == null || bytes.length == 0) {
            return null;
        }

        if (offset < 0) {
            throw new IllegalArgumentException("offset(" + offset + ")");
        }

        if (offset + length > bytes.length) {
            throw new IllegalArgumentException(
                    "offset + length(" + offset + length + ") > bytes.length(" + bytes.length + ")");
        }

        char[] buf = new char[2 * length];
        for (int i = 0; i < length; i++) {
            byte b = bytes[i + offset];
            buf[2 * i + 1] = digits[b & 0xF];
            b = (byte) (b >>> 4);
            buf[2 * i + 0] = digits[b & 0xF];
        }
        return new String(buf);
    }
}
