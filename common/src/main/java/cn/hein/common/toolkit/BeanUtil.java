package cn.hein.common.toolkit;

import java.io.*;

/**
 * @author hein
 */
public class BeanUtil {

    @SuppressWarnings("unchecked")
    public static <T> T deepCopy(T obj) throws IOException, ClassNotFoundException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(outputStream);
        oos.writeObject(obj);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(inputStream);
        return (T) ois.readObject();
    }
}
