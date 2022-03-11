package encrypt;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;

/**
 * Base64编解码<br/>
 * 关于Base64介绍可以看：http://zh.wikipedia.org/wiki/Base64<br/>
 * 普通Base64编码会每隔76个字符增加一个\r\n的换行符，并且含有+/=字符，<br/>
 * 使得不适合作为HTTP的请求参数，所以也提供了encodeExt的方法，在编码的时候，<br/>
 * 1. 不增加\r\n的换行符<br/>
 * 2. 把+/=分别替换成-_.字符，使得编码串可以作为HTTP请求参数<br/>
 * 解码函数能够同时处理这两种编码方式。
 *
 * @author wangqiang
 * @since 2013-07-06
 */

public class Base64 {

    private static final Charset DEFAULT = Charset.defaultCharset();
    private static final Charset GBK = Charset.forName("GBK");
    private static final Charset UTF8 = Charset.forName("UTF-8");
    private static final String Base64 = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=";
    private static final String Base64Ext = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-_.";
    private static final short[] Base64DecodeChars = new short[255];
    private static final char[] Base64Chars = Base64.toCharArray();
    private static final char[] Base64ExtChars = Base64Ext.toCharArray();

    static {
        for (int i = 0; i < 255; i++) {
            Base64DecodeChars[i] = -1;
        }
        for (int i = 0, len = Base64.length(); i < len; i++) {
            Base64DecodeChars[Base64.charAt(i)] = (short) i;
        }
        for (int i = 0, len = Base64Ext.length(); i < len; i++) {
            Base64DecodeChars[Base64Ext.charAt(i)] = (short) i;
        }
    }

    private Base64() {
    }

    /**
     * 把一个字节数组进行Base64编码。每76个字符加一个\r\n换行符，编码后的<br/>
     * 字符串中会含有+/=字符，不适合作为http的get参数来使用。
     *
     * @param b 要编码的字节暑促，要求非空
     * @return 编码后的字符串
     */
    public static String encode(byte[] b) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();

        int count = 0;
        short[] s = new short[3];
        short[] t = new short[4];
        for (int i = 0; i < (b.length + 2) / 3; i++) {
            for (int j = 0; j < 3; j++) {
                if ((i * 3 + j) < b.length) {
                    s[j] = (short) (b[i * 3 + j] & 0xFF);
                } else {
                    s[j] = -1;
                }
            }

            t[0] = (short) (s[0] >> 2);
            if (s[1] == -1) {
                t[1] = (short) (((s[0] & 0x3) << 4));
            } else {
                t[1] = (short) (((s[0] & 0x3) << 4) + (s[1] >> 4));
            }
            if (s[1] == -1) {
                t[2] = t[3] = 64;
            } else if (s[2] == -1) {
                t[2] = (short) (((s[1] & 0xF) << 2));
                t[3] = 64;
            } else {
                t[2] = (short) (((s[1] & 0xF) << 2) + (s[2] >> 6));
                t[3] = (short) (s[2] & 0x3F);
            }

            //每76个字符增加一个\r\n的换行
            int sz = count % 76;
            for (int j = 0; j < 4; j++) {
                os.write(Base64Chars[t[j]]);
                if (sz + j == 75) {
                    os.write('\r');
                    os.write('\n');
                }
            }
            count += 4;
        }
        return new String(os.toByteArray(), DEFAULT);
    }

    /**
     * 把一个字节数组进行Base64EXt编码。和Base64相比，会有如下不同：<br/>
     * 1. 不再每76个字符添加\r\n换行符
     * 2. 把+/=分别换成-_.字符，使得编码后的字符创可以用作http的get参数
     *
     * @param b 要编码的字节暑促，要求非空
     * @return 编码后的字符串
     */
    public static String encodeExt(byte[] b) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();

        short[] s = new short[3];
        short[] t = new short[4];
        for (int i = 0; i < (b.length + 2) / 3; i++) {
            for (int j = 0; j < 3; j++) {
                if ((i * 3 + j) < b.length) {
                    s[j] = (short) (b[i * 3 + j] & 0xFF);
                } else {
                    s[j] = -1;
                }
            }

            t[0] = (short) (s[0] >> 2);
            if (s[1] == -1) {
                t[1] = (short) (((s[0] & 0x3) << 4));
            } else {
                t[1] = (short) (((s[0] & 0x3) << 4) + (s[1] >> 4));
            }
            if (s[1] == -1) {
                t[2] = t[3] = 64;
            } else if (s[2] == -1) {
                t[2] = (short) (((s[1] & 0xF) << 2));
                t[3] = 64;
            } else {
                t[2] = (short) (((s[1] & 0xF) << 2) + (s[2] >> 6));
                t[3] = (short) (s[2] & 0x3F);
            }

            for (int j = 0; j < 4; j++) {
                os.write(Base64ExtChars[t[j]]);
            }
        }
        return new String(os.toByteArray(), DEFAULT);
    }

    /**
     * 把Base64编码的字符串解码成字节数组，具体如何由字节数组构造字符串，业务层自己来做。
     * 也可以使用 {@link #decodeGBK(String) } 或者 {@link #decodeUTF8(String)}<br/>
     *
     * @param str A String containing the encoded data
     * @return An array containing the binary data, or null if the string is invalid
     */
    public static byte[] decode(String str) {
        byte[] raw = str.getBytes(DEFAULT);
        ByteArrayOutputStream bs = new ByteArrayOutputStream(raw.length * 4 / 3);
        for (byte b : raw) {
            if (b == 10 || b == 13) {
                continue;
            }
            bs.write(b);
        }
        byte[] in = bs.toByteArray();
        int length = in.length;
        //如果解析出来长度不是4的整数倍，说明可能传输过程中丢失了某些字符，那就尽可能的解析
        if (length % 4 != 0) {
            length -= (length % 4);
        }

        bs.reset();
        DataOutputStream ds = new DataOutputStream(bs);

        short[] s = new short[4];
        short[] t = new short[3];
        for (int i = 0; i < (length + 3) / 4; i++) {
            for (int j = 0; j < 4; j++) {
                s[j] = Base64DecodeChars[in[i * 4 + j]];
            }

            t[0] = (short) ((s[0] << 2) + (s[1] >> 4));
            if (s[2] == 64) {
                t[1] = t[2] = (short) (-1);
                if ((s[1] & 0xF) != 0) {
                    //遇到错误不再继续解析
                    break;
                }
            } else if (s[3] == 64) {
                t[1] = (short) (((s[1] << 4) + (s[2] >> 2)) & 0xFF);
                t[2] = (short) (-1);
                if ((s[2] & 0x3) != 0) {
                    //遇到错误不再继续解析
                    break;
                }
            } else {
                t[1] = (short) (((s[1] << 4) + (s[2] >> 2)) & 0xFF);
                t[2] = (short) (((s[2] << 6) + s[3]) & 0xFF);
            }

            try {
                for (int j = 0; j < 3; j++) {
                    if (t[j] >= 0) {
                        ds.writeByte(t[j]);
                    }
                }
            } catch (IOException ignored) {
                // ignore exception
            }
        }
        return bs.toByteArray();
    }

    /**
     * 按照GBK编码先获取要编码的byte数组然后再base64编码，长度上会短一些
     *
     * @param str 希望用gbk编码的字符串
     * @return base64编码后的字符串
     */
    public static String encodeGBK(String str) {
        return encode(str.getBytes(GBK));
    }

    /**
     * 按照UTF8编码先获取要编码的byte数组然后再base64编码，长度上会长一些
     *
     * @param str 希望用utf8编码的字符串
     * @return base64编码后的字符串
     */
    public static String encodeUTF8(String str) {
        return encode(str.getBytes(UTF8));
    }

    /**
     * 按照GBK编码先获取要编码的byte数组然后再base64Ext编码，长度上会短一些。<br/>
     * 编码后的字符串可以作为HTTP的请求参数进行传递
     *
     * @param str 希望用gbk编码的字符串
     * @return base64编码后的字符串
     */
    public static String encodeExtGBK(String str) {
        return encodeExt(str.getBytes(GBK));
    }

    /**
     * 按照UTF8编码先获取要编码的byte数组然后再base64Ext编码，长度上会长一些。<br/>
     * 编码后的字符串可以作为HTTP的请求参数进行传递
     *
     * @param str 希望用utf8编码的字符串
     * @return base64编码后的字符串
     */
    public static String encodeExtUTF8(String str) {
        return encodeExt(str.getBytes(UTF8));
    }

    /**
     * 字符串str解码后的byte数组之前是用GBK编码获取的，必须指定GBK编码才能正确解析
     *
     * @param str Base64编码后的字符串
     * @return 以GBK编码获取的字符串
     */
    public static String decodeGBK(String str) {
        return new String(decode(str), GBK);
    }

    /**
     * 字符串str解码后的byte数组之前是用UTF8编码获取的，必须指定UTF8编码才能正确解析
     *
     * @param str Base64编码后的字符串
     * @return 以UTF8编码获取的字符串
     */
    public static String decodeUTF8(String str) {
        return new String(decode(str), UTF8);
    }
}
