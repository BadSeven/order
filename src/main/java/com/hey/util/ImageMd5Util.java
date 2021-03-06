package com.hey.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by heer on 2018/6/20.
 */
public class ImageMd5Util {

    private static final Logger logger = LoggerFactory.getLogger(ImageMd5Util.class);

    protected static char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6','7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
    protected static MessageDigest messagedigest = null;
    static {
        try {
            messagedigest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public static String getFileMD5String(File file) throws IOException {
        InputStream fis;
        fis = new FileInputStream(file);
        byte[] buffer = new byte[1024];
        int numRead = 0;
        while ((numRead = fis.read(buffer)) > 0) {
            messagedigest.update(buffer, 0, numRead);
        }
        fis.close();
        return bufferToHex(messagedigest.digest());
    }

    public static String getStringMD5(String str){
        byte[] buffer=str.getBytes();
        messagedigest.update(buffer);
        return bufferToHex(messagedigest.digest());
    }

    public static String bufferToHex(byte bytes[]) {
        return bufferToHex(bytes, 0, bytes.length);
    }

    private static String bufferToHex(byte bytes[], int m, int n) {
        StringBuffer stringbuffer = new StringBuffer(2 * n);
        int k = m + n;
        for (int l = m; l < k; l++) {
            appendHexPair(bytes[l], stringbuffer);
        }
        return stringbuffer.toString();
    }

    private static void appendHexPair(byte bt, StringBuffer stringbuffer) {
        char c0 = hexDigits[(bt & 0xf0) >> 4];// 取字节中高 4 位的数字转换
        // 为逻辑右移，将符号位一起右移,此处未发现两种符号有何不同
        char c1 = hexDigits[bt & 0xf];// 取字节中低 4 位的数字转换
        stringbuffer.append(c0);
        stringbuffer.append(c1);
    }

    public static String getMD5(String URLName){
        String name="";
        try {
            URL url = new URL(URLName);
            InputStream inputStream = new BufferedInputStream(url.openStream());
            byte[] bytes = new byte[1024];
            int len = 0;
            MessageDigest messagedigest = MessageDigest.getInstance("MD5");
            while ((len = inputStream.read(bytes)) > 0) {
                messagedigest.update(bytes, 0, len);
            }
            name = bufferToHex(messagedigest.digest());
            inputStream.close();
        } catch (MalformedURLException e) {
            logger.info(e.getMessage());
        } catch (IOException e) {
            logger.info(e.getMessage());
        } catch (NoSuchAlgorithmException e) {
            logger.info(e.getMessage());
        }
        return name;
    }
}
