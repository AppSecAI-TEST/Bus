package com.speedata.javatest;

import com.google.common.base.Joiner;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemWriter;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import sun.security.rsa.RSAPrivateCrtKeyImpl;

/**
 * Created by zhijian.zhang@chelaile.net.cn on 17/7/7.
 */
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
    public static RSAKey genRsaKeyByOpenssl(String outPath) throws Exception {
        String tmpDir = System.getProperty("java.io.tmpdir");
        String pkcs8PrivateKeyPath = outPath + File.separator + "pkcs8.private.key";
        String pkcs1PrivateKeyPath = outPath + File.separator + "pkcs1.private.key";
        String publicKeyPath = outPath + File.separator + "public.key";
        new File(pkcs8PrivateKeyPath).delete();
        new File(pkcs1PrivateKeyPath).delete();
        new File(publicKeyPath).delete();
        String cmd = Joiner.on("\n").join("#!/bin/bash", "cd " + outPath + ";", "openssl genrsa -out pkcs1.private.key 1024;",
                "openssl rsa -in pkcs1.private.key -pubout -out public.key;",
                "openssl pkcs8 -topk8 -inform PEM -in pkcs1.private.key -outform PEM -nocrypt -out pkcs8.private.key;",
                "echo 'ok'");
        System.out.println(cmd);
        File cmdFile = new File(tmpDir + File.separator + "rsa.sh");
        FileUtils.writeStringToFile(cmdFile, cmd, "utf-8");
        cmdFile.setExecutable(true);
        Process process = null;
        List<String> processList = new ArrayList<>();
        try {
            System.out.println(cmdFile.getAbsolutePath());
            process = Runtime.getRuntime().exec(cmdFile.getAbsolutePath());
            BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = "";
            while ((line = input.readLine()) != null) {
                processList.add(line);
            }
            input.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String publicKeyPem = FileUtils.readFileToString(new File(publicKeyPath), "utf-8");
        String pkcs1PrivateKeyPem = FileUtils.readFileToString(new File(pkcs1PrivateKeyPath), "utf-8");
        String pkcs8PrivateKeyPem = FileUtils.readFileToString(new File(pkcs8PrivateKeyPath), "utf-8");
        if (StringUtils.isNotBlank(publicKeyPem) && StringUtils.isNotBlank(pkcs1PrivateKeyPem) &&
                StringUtils.isNotBlank(pkcs8PrivateKeyPem)) {
            return RSAKey.create(pem2SignString(pkcs1PrivateKeyPath), pem2SignString(pkcs8PrivateKeyPath), pem2SignString(publicKeyPath));
        }
        throw new Exception("生成RSA 公钥、PKCS1私钥、PKCS8私钥 失败!");
    }

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
        return Base64.getEncoder().encodeToString(key.getEncoded());
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
        return Base64.getEncoder().encodeToString(key.getEncoded());
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
        byte[] keyBytes = Base64.getDecoder().decode(privateKey);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM, new BouncyCastleProvider());
        PrivateKey privateK = keyFactory.generatePrivate(pkcs8KeySpec);
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initSign(privateK);
        signature.update(data);
        return Base64.getEncoder().encodeToString(signature.sign());
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
        byte[] keyBytes = Base64.getDecoder().decode(publicKey);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM, new BouncyCastleProvider());
        PublicKey publicK = keyFactory.generatePublic(keySpec);
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initVerify(publicK);
        signature.update(data);
        return signature.verify(Base64.getDecoder().decode(sign));
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
    public static byte[] decryptByPrivateKey(byte[] encryptedData, String privateKey) {
        System.out.println("000000000000000000000");
        byte[] keyBytes = Base64.getDecoder().decode(privateKey);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = null;
        try {
            keyFactory = KeyFactory.getInstance(KEY_ALGORITHM, new BouncyCastleProvider());
            Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);
            Cipher cipher = Cipher.getInstance(KEY_ALGORITHM_CONFIG, new BouncyCastleProvider());
            cipher.init(Cipher.DECRYPT_MODE, privateK);
            return write2ByteArray(cipher, encryptedData, false);
        } catch (NoSuchAlgorithmException e) {
            System.out.println("11111111111111");
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            System.out.println("222222222222222");
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            System.out.println("33333333333333");
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            System.out.println("444444444444444");
            e.printStackTrace();
        } catch (BadPaddingException e) {
            System.out.println("5555555555555555");
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            System.out.println("6666666666666666");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("77777777777777777");
            e.printStackTrace();
        }
        return null;
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
        byte[] keyBytes = Base64.getDecoder().decode(publicKey);
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
        byte[] keyBytes = Base64.getDecoder().decode(publicKey);
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
        byte[] keyBytes = Base64.getDecoder().decode(privateKey);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM, new BouncyCastleProvider());
        Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);
        Cipher cipher = Cipher.getInstance(KEY_ALGORITHM_CONFIG, new BouncyCastleProvider());
        cipher.init(Cipher.ENCRYPT_MODE, privateK);

        byte[] bs = write2ByteArray(cipher, data, true);
        return bs;
    }


    public static String convert2Pem(String privateKey) throws InvalidKeyException, IOException {
        PemObject pemObject = new PemObject("RSA PRIVATE KEY", RSAPrivateCrtKeyImpl.newKey(org.apache.commons.codec.binary.Base64.decodeBase64(privateKey)).getEncoded());
        StringWriter stringWriter = new StringWriter();
        PemWriter pemWriter = new PemWriter(stringWriter);
        pemWriter.writeObject(pemObject);
        pemWriter.close();
        String pemString = stringWriter.toString();
        return pemString;
    }


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

    public static String pem2SignString(String pemPath) throws IOException {
        List<String> s = FileUtils.readLines(new File(pemPath), "utf-8");
        return Joiner.on("").join(s.subList(1, s.size() - 1));
    }


}

