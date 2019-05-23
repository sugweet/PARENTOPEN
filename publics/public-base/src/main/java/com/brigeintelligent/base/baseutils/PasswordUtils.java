package com.brigeintelligent.base.baseutils;

import org.apache.shiro.codec.Base64;
import org.apache.shiro.crypto.AesCipherService;
import org.apache.shiro.util.ByteSource;

/**
 * @Description：密码加密解密工具类（采用AES加密与解密）
 * @Author：Sugweet
 * @Time：2019/4/25 17:34
 */
public class PasswordUtils {
    // 定义加密密钥，要求秘钥长度为16位的字节数组
  private static final byte[] key = "7758258520131400".getBytes();
  private static final AesCipherService aes = new AesCipherService();

    public PasswordUtils() {
    }

    // 密码加密
    public static String passwordEncode(String password) {
        ByteSource byteSource = aes.encrypt(password.getBytes(), key);
        // Base64加密
        return byteSource.toBase64();
    }

    // 密码解密
    public static String passwordDecode(String password) {
        // aes中的Base64解密
        ByteSource byteSource = aes.decrypt(Base64.decode(password), key);
        return new String(byteSource.getBytes());
    }

    public static void main(String[] args) {
        String encode = PasswordUtils.passwordEncode("123456");
        System.out.println(encode+"======"+encode.length());
        String decode = PasswordUtils.passwordDecode(encode);
        System.out.println(decode);
    }
}
