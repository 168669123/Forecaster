package wjh.projects.common.util;

public class GeoUtil {
    /**
     * 地球半径，单位 km
     */
    private static final double EARTH_RADIUS = 6371.000;

    /**
     * 根据经纬度来计算两地间的直线距离
     *
     * @return 两地直线距离，单位 m
     */
    public static double calculateLinearDistance(double xLongitude, double yLongitude, double xLatitude, double yLatitude) {
        xLongitude = Math.toRadians(xLongitude);
        yLongitude = Math.toRadians(yLongitude);
        xLatitude = Math.toRadians(xLatitude);
        yLatitude = Math.toRadians(yLatitude);
        // 经度之差
        double a = xLatitude - yLatitude;
        // 维度之差
        double b = xLongitude - yLongitude;
        // 计算两点距离的公式
        double distance = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) +
                Math.cos(xLatitude) * Math.cos(yLatitude) * Math.pow(Math.sin(b / 2), 2)));
        // 结果保留四位小数
        return Math.round(distance * EARTH_RADIUS * 10000) / 10.0;
    }
}
