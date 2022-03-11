package encrypt;

import java.security.MessageDigest;
import java.util.Random;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * AES/ECB/PKCS5Padding方案加解密工具
 */
public class AES {

    /**
     * 解密
     *
     * @param eSrc 密文
     * @param sKey 密钥
     */
    public static String decrypt(String eSrc, String sKey) {
        try {

            byte[] raw = sKey.getBytes("GBK");
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
            byte[] encrypted1 = hex2byte(eSrc.getBytes());
            try {
                byte[] original = cipher.doFinal(encrypted1);
                String originalString = new String(original);
                originalString = new String(originalString.getBytes(), "UTF-8");

                return originalString;
            } catch (Exception e) {
                return null;
            }
        } catch (Exception ex) {
            return null;
        }
    }

    public static String decrypt(String sSrc, String sKey, String ivParameter) throws Exception {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(sKey.getBytes("GBK"), "AES"),
                    new IvParameterSpec(ivParameter.getBytes("GBK")));
            byte[] original = cipher.doFinal(hex2byte(sSrc.getBytes("GBK")));
            return new String(original, "GBK");
        } catch (Exception ex) {
            return null;
        }
    }

    /**
     * 加密
     *
     * @param sSrc 原文
     * @param sKey 密钥
     */
    public static String encrypt(String sSrc, String sKey) throws Exception {

        byte[] raw = sKey.getBytes("GBK");
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);

        byte[] encrypted = cipher.doFinal(sSrc.getBytes());
        return byte2hex(encrypted).toLowerCase();
    }

    public static String encrypt(String sSrc, String sKey, String ivParameter) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(sKey.getBytes("GBK"), "AES"),
                new IvParameterSpec(ivParameter.getBytes("GBK")));
        byte[] encrypted = cipher.doFinal(sSrc.getBytes("GBK"));
        return byte2hex(encrypted);//此处使用BASE64做转码。
    }

    public static String encryptShaKey(String content, String key, String ivParameter) throws Exception {
        byte[] input = content.getBytes("UTF-8");

        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] thedigest = md.digest(key.getBytes("UTF-8"));
        SecretKeySpec skc = new SecretKeySpec(thedigest, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, skc, new IvParameterSpec(ivParameter.getBytes("UTF-8")));

        byte[] cipherText = new byte[cipher.getOutputSize(input.length)];
        int ctLength = cipher.update(input, 0, input.length, cipherText, 0);
        ctLength += cipher.doFinal(cipherText, ctLength);
        return byte2hex(cipherText);
    }

    public static String decryptShaKey(String encrypted, String key, String ivParameter) throws Exception {
        byte[] keyb = key.getBytes("UTF-8");
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] thedigest = md.digest(keyb);
        SecretKeySpec skey = new SecretKeySpec(thedigest, "AES");
        Cipher dcipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        dcipher.init(Cipher.DECRYPT_MODE, skey, new IvParameterSpec(ivParameter.getBytes()));
        byte[] clearbyte = dcipher.doFinal(hex2byte(encrypted.getBytes("UTF-8")));
        return new String(clearbyte, "UTF-8");
    }

    /**
     * 16进制字符转字节
     */
    public static byte[] hex2byte(byte[] b) {
        if ((b.length % 2) != 0) {
            throw new IllegalArgumentException("密文长度错误");
        }
        byte[] b2 = new byte[b.length / 2];
        for (int n = 0; n < b.length; n += 2) {
            String item = new String(b, n, 2);
            b2[n / 2] = (byte) Integer.parseInt(item, 16);
        }
        return b2;
    }

    /**
     * 字节转16进制字符
     */
    public static String byte2hex(byte[] b) {
        String hs = "";
        String stmp = "";
        for (int n = 0; n < b.length; n++) {
            stmp = (Integer.toHexString(b[n] & 0XFF));
            if (stmp.length() == 1) {
                hs = hs + "0" + stmp;
            } else {
                hs = hs + stmp;
            }
        }
        return hs.toUpperCase();
    }

    /**
     * 生成对称加密的秘钥
     */
    public static String generateKey() {
        Random random = new Random();
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < 16; i++) {
            int val = random.nextInt(16);
            if (val < 10) {
                result.append(val);
            } else {
                char c = (char) (55 + val);
                result.append(c);
            }
        }
        return result.toString();
    }
}