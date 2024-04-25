package wjh.projects.common.constants;

import lombok.Getter;

@Getter
public enum PropertiesEnum {
    GPS_DELAY_UPLOAD_THRESHOLD("gps.delayUploadThreshold", Integer.class),
    ESTIMATE_INTERVAL("estimate.interval", Integer.class),
    TRANSPORT_SPEED("transport.speed", Integer.class),
    ALARM_THRESHOLD("alarm.threshold", Integer.class),
    BAIDU_AK("baidu.ak", String.class),
    METRICS_APPLICATION("metrics.application", String.class);
    private final String key;
    public final Class<?> clazz;

    PropertiesEnum(String key, Class<?> clazz) {
        this.key = key;
        this.clazz = clazz;
    }
}
