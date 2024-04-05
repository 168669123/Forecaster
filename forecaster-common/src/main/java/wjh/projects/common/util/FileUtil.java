package wjh.projects.common.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class FileUtil {
    private static volatile FileUtil fileUtils;
    private final Map<String, FileChannel> fileChannelMap;
    private final Map<String, ByteBuffer> byteBufferMap;

    private FileUtil() {
        fileChannelMap = new HashMap<>();
        byteBufferMap = new HashMap<>();
    }

    public static FileUtil getInstance() {
        if (fileUtils == null) {
            synchronized (FileUtil.class) {
                if (fileUtils == null) {
                    fileUtils = new FileUtil();
                }
            }
        }
        return fileUtils;
    }

    /**
     * 1、文件中数据是以 JsonArray 的形式存储的
     * 2、从目标文件中获取固定长度的 JsonArray
     * 3、当再次调用时，会依次获取剩余数据
     * 4、如果文件数据已经全部读取，则返回 null
     */
    public synchronized String getFixedSizeInTurnFromJsonArray(String filePath, int size) {
        if (!(fileChannelMap.containsKey(filePath) && byteBufferMap.containsKey(filePath))) {
            try {
                fileChannelMap.put(filePath, new FileInputStream(filePath).getChannel());
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
            byteBufferMap.put(filePath, ByteBuffer.allocate(1));
        }

        FileChannel channel = fileChannelMap.get(filePath);
        ByteBuffer buffer = byteBufferMap.get(filePath);
        StringBuilder sb = new StringBuilder("[");
        while (size > 0) {
            try {
                if (channel.read(buffer) == -1) {
                    channel.close();
                    return null;
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            buffer.flip();
            boolean[] startEnd = new boolean[2];
            for (int i = 0; i < buffer.limit(); i++) {
                startEnd[0] = buffer.get(i) == '{' || startEnd[0];
                startEnd[1] = (startEnd[0] && buffer.get(i) == '}') || startEnd[1];
                if (startEnd[0] && startEnd[1]) {
                    int length = i - buffer.position() + 1;
                    ByteBuffer read = ByteBuffer.allocate(length);
                    for (int j = 0; j < length; j++)
                        read.put(buffer.get());

                    read.flip();
                    String string = StandardCharsets.UTF_8.decode(read).toString();
                    string = string.substring(1);
                    sb.append(string);
                    Arrays.fill(startEnd, false);
                    size--;
                }
            }
            buffer.compact();
            if (buffer.position() == buffer.limit()) {
                ByteBuffer newBuffer = ByteBuffer.allocate(buffer.capacity() * 2);
                buffer.flip();
                newBuffer.put(buffer);
                buffer = newBuffer;
            }
        }
        sb.append("]");
        byteBufferMap.put(filePath, buffer);
        return sb.toString();
    }
}
