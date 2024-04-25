package wjh.projects.common.util;

import java.io.*;

public class DeepCopyUtil {

    public static <T extends Serializable> T deepCopy(T object, Class<T> clazz) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream)) {
            objectOutputStream.writeObject(object);
        } catch (IOException e) {
            throw new RuntimeException("深拷贝序列化异常");
        }

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        try (ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream)) {
            return clazz.cast(objectInputStream.readObject());
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("深拷贝反序列化异常");
        }
    }
}
