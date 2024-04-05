package wjh.projects.common;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class IdGenerator {
    public static final byte BATCH_NUMBER = 0;
    public static final byte METRICS_ESTIMATE = 1;
    public static final byte METRICS_TRACK = 2;

    private static final Map<Byte, AtomicLong> idMap = new HashMap<>();

    static {
        idMap.put(BATCH_NUMBER, new AtomicLong());
        idMap.put(METRICS_ESTIMATE, new AtomicLong());
        idMap.put(METRICS_TRACK, new AtomicLong());
    }

    public static long nextId(Byte type) {
        return idMap.get(type).getAndIncrement();
    }
}
