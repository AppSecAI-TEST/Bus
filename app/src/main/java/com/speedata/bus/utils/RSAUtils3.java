package com.speedata.bus.utils;

import android.util.Base64;

import org.apache.commons.lang3.tuple.Pair;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
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
import java.util.HashMap;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;

public class RSAUtils3 {
    /**
     * 加密算法RSA
     */
    public static final String KEY_ALGORITHM = "RSA";

    public static final String KEY_ALGORITHM_CONFIG = "RSA/NONE/PKCS1Padding";
    /**
     * 签名算法
     */
    public static final String SIGNATURE_ALGORITHM = "MD5withRSA";
    /**
     * 获取公钥的key
     */
    public static final String PUBLIC_KEY = "RSA_PUBLICK_KEY";

    /**
     * 获取私钥的key PKCS#8
     */
    public static final String PRIVATE_KEY_PKCS8 = "RSA_PRIVATE_KEY_PKCS8";

    /**
     * 获取私钥的key PKCS#1
     */
    public static final String PRIVATE_KEY_PKCS1 = "RSA_PRIVATE_KEY_PKCS1";
    /**
     * RSA最大加密明文大小
     */
    private static final int MAX_ENCRYPT_BLOCK = 117;

    /**
     * RSA最大解密密文大小
     */
    private static final int MAX_DECRYPT_BLOCK = 128;


    /**
     * <p>
     * 生成密钥对(公钥和私钥)
     * </p>
     *
     * @return
     * @throws Exception
     */
    private static Map<String, Object> genKeyPair() throws Exception {
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(KEY_ALGORITHM, new BouncyCastleProvider());
        keyPairGen.initialize(1024);
        KeyPair keyPair = keyPairGen.genKeyPair();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();

        System.out.println(publicKey.getFormat());
        System.out.println(privateKey.getFormat());

        Map<String, Object> keyMap = new HashMap<>(2);
        keyMap.put(PUBLIC_KEY, publicKey);
        keyMap.put(PRIVATE_KEY_PKCS8, privateKey);
        return keyMap;
    }

    /**
     * <p>
     * 获取私钥
     * </p>
     *
     * @param keyMap 密钥对
     * @return
     * @throws Exception
     */
    private static String getPrivateKey(Map<String, Object> keyMap)
            throws Exception {
        Key key = (Key) keyMap.get(PRIVATE_KEY_PKCS8);
//        return Base64.getEncoder().encodeToString(key.getEncoded());
        return Base64.encodeToString(key.getEncoded(), Base64.NO_WRAP);
    }

    /**
     * <p>
     * 获取公钥
     * </p>
     *
     * @param keyMap 密钥对
     * @return
     * @throws Exception
     */
    private static String getPublicKey(Map<String, Object> keyMap)
            throws Exception {
        Key key = (Key) keyMap.get(PUBLIC_KEY);
//        return Base64.getEncoder().encodeToString(key.getEncoded());
        return Base64.encodeToString(key.getEncoded(),Base64.NO_WRAP);
    }

    public static Pair<String, String> genKeyPairString() throws Exception {
        Map<String, Object> pp = genKeyPair();
        return Pair.of(getPublicKey(pp), getPrivateKey(pp));
    }

    /**
     * <p>
     * 用私钥对信息生成数字签名
     * </p>
     *
     * @param data       已加密数据
     * @param privateKey 私钥(BASE64编码)
     * @return
     * @throws Exception
     */
    public static String sign(byte[] data, String privateKey) throws Exception {
//        byte[] keyBytes = Base64.getDecoder().decode(privateKey);
        byte[] keyBytes = Base64.decode(privateKey, Base64.NO_WRAP);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM, new BouncyCastleProvider());
        PrivateKey privateK = keyFactory.generatePrivate(pkcs8KeySpec);
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initSign(privateK);
        signature.update(data);
        return Base64.encodeToString(signature.sign(), Base64.NO_WRAP);
    }

    /**
     * <p>
     * 校验数字签名
     * </p>
     *
     * @param data      已加密数据
     * @param publicKey 公钥(BASE64编码)
     * @param sign      数字签名
     * @return
     * @throws Exception
     */
    public static boolean verify(byte[] data, String publicKey, String sign)
            throws Exception {
//        byte[] keyBytes = Base64.getDecoder().decode(publicKey);
        byte[] keyBytes = Base64.decode(publicKey, Base64.NO_WRAP);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM, new BouncyCastleProvider());
        PublicKey publicK = keyFactory.generatePublic(keySpec);
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initVerify(publicK);
        signature.update(data);
//        return signature.verify(Base64.getDecoder().decode(sign));
        return signature.verify(Base64.decode(sign, Base64.NO_WRAP));
    }

    /**
     * <P>
     * 私钥解密
     * </p>
     *
     * @param encryptedData 已加密数据
     * @param privateKey    私钥(BASE64编码)
     * @return
     * @throws Exception
     */
    public static byte[] decryptByPrivateKey(byte[] encryptedData, String privateKey)
            throws Exception {
//        byte[] keyBytes = Base64.getDecoder().decode(privateKey);
        byte[] keyBytes = Base64.decode(privateKey, Base64.NO_WRAP);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM, new BouncyCastleProvider());
        Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);
        Cipher cipher = Cipher.getInstance(KEY_ALGORITHM_CONFIG, new BouncyCastleProvider());
        cipher.init(Cipher.DECRYPT_MODE, privateK);

        return write2ByteArray(cipher, encryptedData, false);
    }

    /**
     * <p>
     * 公钥解密
     * </p>
     *
     * @param encryptedData 已加密数据
     * @param publicKey     公钥(BASE64编码)
     * @return
     * @throws Exception
     */
    public static byte[] decryptByPublicKey(byte[] encryptedData, String publicKey)
            throws Exception {
//        byte[] keyBytes = Base64.getDecoder().decode(publicKey);
        byte[] keyBytes = Base64.decode(publicKey, Base64.NO_WRAP);
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM, new BouncyCastleProvider());
        Key publicK = keyFactory.generatePublic(x509KeySpec);
        Cipher cipher = Cipher.getInstance(KEY_ALGORITHM_CONFIG, new BouncyCastleProvider());
        cipher.init(Cipher.DECRYPT_MODE, publicK);
        return write2ByteArray(cipher, encryptedData, false);
    }

    /**
     * <p>
     * 公钥加密
     * </p>
     *
     * @param data      源数据
     * @param publicKey 公钥(BASE64编码)
     * @return
     * @throws Exception
     */
    public static byte[] encryptByPublicKey(byte[] data, String publicKey)
            throws Exception {
//        byte[] keyBytes = Base64.getDecoder().decode(publicKey);
        byte[] keyBytes = Base64.decode(publicKey, Base64.NO_WRAP);
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM, new BouncyCastleProvider());
        Key publicK = keyFactory.generatePublic(x509KeySpec);
        // 对数据加密
        Cipher cipher = Cipher.getInstance(KEY_ALGORITHM_CONFIG, new BouncyCastleProvider());
        cipher.init(Cipher.ENCRYPT_MODE, publicK);
        return write2ByteArray(cipher, data, true);
    }


    private static byte[] write2ByteArray(Cipher cipher, byte[] data, boolean encrypt) throws BadPaddingException, IllegalBlockSizeException, IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        int inputLen = data.length;
        int MAX = encrypt ? MAX_ENCRYPT_BLOCK : MAX_DECRYPT_BLOCK;
        // 对数据分段加密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX) {
                cache = cipher.doFinal(data, offSet, MAX);
            } else {
                cache = cipher.doFinal(data, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX;
        }
        byte[] eData = out.toByteArray();
        out.close();
        return eData;
    }


    /**
     * <p>
     * 私钥加密
     * </p>
     *
     * @param data       源数据
     * @param privateKey 私钥(BASE64编码)
     * @return
     * @throws Exception
     */
    public static byte[] encryptByPrivateKey(byte[] data, String privateKey)
            throws Exception {
//        byte[] keyBytes = Base64.getDecoder().decode(privateKey);
        byte[] keyBytes = Base64.decode(privateKey, Base64.NO_WRAP);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM, new BouncyCastleProvider());
        Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);
        Cipher cipher = Cipher.getInstance(KEY_ALGORITHM_CONFIG, new BouncyCastleProvider());
        cipher.init(Cipher.ENCRYPT_MODE, privateK);

        byte[] bs = write2ByteArray(cipher, data, true);
        return bs;
    }


//    public static String convert2Pem(String privateKey) throws InvalidKeyException, IOException {
//        PemObject pemObject = new PemObject("RSA PRIVATE KEY", RSAPrivateCrtKeyImpl.newKey(org.apache.commons.codec.binary.Base64.decodeBase64(privateKey)).getEncoded());
//        StringWriter stringWriter = new StringWriter();
//        PemWriter pemWriter = new PemWriter(stringWriter);
//        pemWriter.writeObject(pemObject);
//        pemWriter.close();
//        String pemString = stringWriter.toString();
//        return pemString;
//    }


    public static class RSAKey {
        private String pkcs1Key;
        private String pkcs8Key;
        private String publicKey;

        public static RSAKey create(String pkcs1Key, String pkcs8Key, String publicKey) {
            RSAKey rsaKey = new RSAKey();
            rsaKey.setPkcs1Key(pkcs1Key);
            rsaKey.setPkcs8Key(pkcs8Key);
            rsaKey.setPublicKey(publicKey);
            return rsaKey;
        }

        public String getPkcs1Key() {
            return pkcs1Key;
        }

        public void setPkcs1Key(String pkcs1Key) {
            this.pkcs1Key = pkcs1Key;
        }

        public String getPkcs8Key() {
            return pkcs8Key;
        }

        public void setPkcs8Key(String pkcs8Key) {
            this.pkcs8Key = pkcs8Key;
        }

        public String getPublicKey() {
            return publicKey;
        }

        public void setPublicKey(String publicKey) {
            this.publicKey = publicKey;
        }
    }



}

