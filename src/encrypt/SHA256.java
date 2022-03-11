package encrypt;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Description:
 * <br/>sha2的一种经典实现，安全性高于sha1
 * <br/>Date: 2020/1/14
 *
 * @author qiangwang
 */
public class SHA256 {

    /**
     * 计算摘要
     */
    public static String encrypt(String decript) {
        try {
            MessageDigest digest = MessageDigest
                    .getInstance("SHA-256");
            digest.update(decript.getBytes());
            byte[] messageDigest = digest.digest();
            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            // 字节数组转换为十六进制数
            for (int i = 0; i < messageDigest.length; i++) {
                String shaHex = Integer.toHexString(messageDigest[i] & 0xFF);
                if (shaHex.length() < 2) {
                    hexString.append(0);
                }
                hexString.append(shaHex);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

}
