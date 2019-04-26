package com.brigeintelligent.api.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

/**
 * @Description：id自动生成工具类
 * @Author：Sugweet
 * @Time：2019/4/22 14:29
 */
public class IDGetGenerator {
    public static final String gen() {
        return UUID.randomUUID().toString().replaceAll( "-", "" ).toUpperCase();
    }

    public static String gen( String source ) {
        StringBuilder buf = new StringBuilder( "" );
        try {
            MessageDigest md = MessageDigest.getInstance( "MD5" );
            md.update( source.getBytes() );
            byte[] b = md.digest();
            int i;
            for (byte value : b) {
                i = value;
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
        }
        catch ( NoSuchAlgorithmException e ) {
            e.printStackTrace();
        }
        return buf.toString().toUpperCase();
    }
}
