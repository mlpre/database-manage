package io.github.mlpre.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class ObjectUtil {

    public static byte[] objectToBytes(Object object) {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream)) {
            objectOutputStream.writeObject(object);
            objectOutputStream.flush();
            return byteArrayOutputStream.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Object bytesToObject(byte[] bytes) {
        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
             ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream)) {
            return objectInputStream.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <T> List<T> toList(Object object, Class<T> clazz) {
        List<T> result = new ArrayList<>();
        if (object instanceof List<?> list) {
            list.forEach(temp -> result.add(clazz.cast(temp)));
        }
        return result;
    }

}
