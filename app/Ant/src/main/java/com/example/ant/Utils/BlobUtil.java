package com.example.ant.Utils;

import java.io.*;

/**
 * byte[]类型操作类
 */
public class BlobUtil {

    /**
     * 把object对象序列化为二进制字节数组
     * @param object
     * @return
     */
    public static byte[] setObject(Object object) {
        ByteArrayOutputStream baos = null;
        ObjectOutputStream out = null;
        try {
            baos = new ByteArrayOutputStream();
            out = new ObjectOutputStream(baos);
            out.writeObject(object);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (baos != null) {
                try {
                    baos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return baos.toByteArray();
    }

    /**
     * 把二进制字节数组反序列化为object对象
     * object当中的每个javaBean对象都必须实现序列化
     * 最外层的类必须生成一个序列化ID
     * @param bytes
     * @return
     */
    public static Object getObject(byte[] bytes) {
        Object obj = null;
        ByteArrayInputStream bais = null;
        ObjectInputStream in = null;
        try {
            bais = new ByteArrayInputStream(bytes);
            in = new ObjectInputStream(bais);
            obj = in.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (bais != null) {
                try {
                    bais.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return obj;
    }
}
