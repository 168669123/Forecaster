package wjh.projects.common;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public abstract class Environment {
    private static final Properties properties;

    static {
        try {
            InputStream in = Environment.class.getResourceAsStream("/application-common.properties");
            properties = new Properties();
            properties.load(in);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static int getGpsDelayUploadThreshold() {
        String delayUploadThreshold = properties.getProperty("gps.delayUploadThreshold");
        // GPS 信息延迟上传阈值默认为30分钟
        return delayUploadThreshold == null ? 30 : Integer.parseInt(delayUploadThreshold);
    }

    public static int getEstimateInterval() {
        String estimateInterval = properties.getProperty("estimate.interval");
        // 预计到计算间隔默认为5分钟
        return estimateInterval == null ? 5 : Integer.parseInt(estimateInterval);
    }

    public static int getTransportSpeed() {
        String transportSpeed = properties.getProperty("transport.speed");
        // 运输速度默认为75 km/h
        return transportSpeed == null ? 75 : Integer.parseInt(transportSpeed);
    }

//    public static Predictor getPredictor() {
//        String strategyName = properties.getProperty("predict.strategy");
//        // 预计到算法策略默认为 SIMPLE
//        return strategyName == null ? PredictorFactory.get("SIMPLE") : PredictorFactory.get(strategyName);
//    }

    public static int getAlarmThreshold() {
        String alarmThreshold = properties.getProperty("alarm.threshold");
        // 预警阈值（预计到时间与计划到时间的差值绝对值）默认为30分钟
        return alarmThreshold == null ? 30 : Integer.parseInt(alarmThreshold);
    }

    public static String getBaiduAk() {
        return properties.getProperty("baidu.ak");
    }
}
