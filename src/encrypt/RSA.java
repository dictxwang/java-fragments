package encrypt;

import java.io.ByteArrayOutputStream;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import javax.crypto.Cipher;

/**
 * Description:
 * <br/>RSA加密工具类
 * <br/>Date: 2020/1/14
 *
 * @author qiangwang
 */
public class RSA {

    private static final String SIGNATURE_ALGORITHM = "MD5withRSA";
    // 最大加密块长度
    private static final int MAX_ENCRYPT_BLOCK = 117;
    // 最大解密块长度
    private static final int MAX_DECRYPT_BLOCK = 128;

    // rsa的密钥对
    public static class RSAKeyPair {
    	
    	private String publicKey;
    	private String privateKey;
    	
    	RSAKeyPair(String publickKey, String privateKey) {
    		this.publicKey = publickKey;
    		this.privateKey = privateKey;
    	}
    	
    	public String getPublicKey() {
    		return this.publicKey;
    	}
    	
    	public String getPrivateKey() {
    		return this.privateKey;
    	}
    }
    
    /**
     * 生成公私钥
     *
     * @return first:公钥， second:私钥
     * @throws Exception
     */
    public static RSAKeyPair generateKeyPair() throws Exception {
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
        keyPairGen.initialize(1024);
        KeyPair keyPair = keyPairGen.generateKeyPair();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        return new RSAKeyPair(Base64.encode(publicKey.getEncoded()), Base64.encode(privateKey.getEncoded()));
    }

    /**
     * 私钥签名
     *
     * @param data 明文数据
     * @param privateKey 私钥
     */
    public static String sign(byte[] data, String privateKey) throws Exception {
        byte[] keyBytes = Base64.decode(privateKey);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateK = keyFactory.generatePrivate(pkcs8KeySpec);
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initSign(privateK);
        signature.update(data);
        return Base64.encode(signature.sign());
    }

    /**
     * 公钥延签
     *
     * @param data 明文数据
     * @param publicKey 公钥
     * @param sign 签名
     */
    public static boolean verify(byte[] data, String publicKey, String sign)
            throws Exception {
        byte[] keyBytes = Base64.decode(publicKey);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicK = keyFactory.generatePublic(keySpec);
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initVerify(publicK);
        signature.update(data);
        return signature.verify(Base64.decode(sign));
    }

    /**
     * 私钥解密
     *
     * @param encryptedData 公钥加密数据
     * @param privateKey 私钥
     */
    public static byte[] decryptByPrivateKey(byte[] encryptedData, String privateKey)
            throws Exception {
        byte[] keyBytes = Base64.decode(privateKey);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, privateK);
        int inputLen = encryptedData.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段解密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
                cache = cipher.doFinal(encryptedData, offSet, MAX_DECRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_DECRYPT_BLOCK;
        }
        byte[] decryptedData = out.toByteArray();
        out.close();
        return decryptedData;
    }

    /**
     * 公钥加密
     *
     * @param data 明文数据
     * @param publicKey 公钥
     */
    public static byte[] encryptByPublicKey(byte[] data, String publicKey)
            throws Exception {
        byte[] keyBytes = Base64.decode(publicKey);
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        Key publicK = keyFactory.generatePublic(x509KeySpec);
        // 对数据加密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, publicK);
        int inputLen = data.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段加密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
                cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(data, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_ENCRYPT_BLOCK;
        }
        byte[] encryptedData = out.toByteArray();
        out.close();
        return encryptedData;
    }

    /**
     * 示例代码
     */
    public static void main(String[] args) {
        try {
            RSAKeyPair keyPair = generateKeyPair();
            String publicKey = keyPair.getPublicKey();
            String privateKey = keyPair.getPrivateKey();
            System.out.printf("--public key--\n%s\n--private key--\n%s\n", publicKey, privateKey);
            System.out.printf("public key length:%d\n", publicKey.length());
            System.out.printf("private key length:%d\n", privateKey.length());

            String message = "123456";
            String sign = sign(message.getBytes(), privateKey);
            System.out.println("sign: " + sign);
            System.out.println("verify: " + verify(message.getBytes(), publicKey, sign));

            byte[] encryptedPublicKey = encryptByPublicKey(message.getBytes(), publicKey);
            System.out.println("encryptedByPublicKey: " + new String(encryptedPublicKey));
            byte[] decryptedPrivateKey = decryptByPrivateKey(encryptedPublicKey, privateKey);
            System.out.println("decryptedPrivateKey: " + new String(decryptedPrivateKey));

            String test = "1234567890";
            long st = System.currentTimeMillis();
            for (int i = 0; i < 100; i++) {
                byte[] encrypted = encryptByPublicKey(test.getBytes(), publicKey);
                decryptByPrivateKey(encrypted, privateKey);
            }
            long cost = System.currentTimeMillis() - st;
            System.out.println("cost: " + cost);
        } catch (Exception exp) {
            exp.printStackTrace();
        }
    }
}
