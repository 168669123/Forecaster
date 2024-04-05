package wjh.projects.infrastructure.rpc;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import wjh.projects.common.Environment;
import wjh.projects.common.util.HttpUtil;
import wjh.projects.common.util.UrlUtil;
import wjh.projects.domain.transportVehicle.model.vo.LocationVO;
import wjh.projects.domain.transportVehicle.rpc.LocationFacadeClient;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 百度地图 API 服务
 */
@Service
public class BaiduLocationFacadeClientImpl implements LocationFacadeClient {
    private static final Logger logger = LoggerFactory.getLogger(BaiduLocationFacadeClientImpl.class);

    /**
     * 地理编码服务
     */
    @Override
    public LocationVO geocode(String address) {
        // 调用百度地图-地理编码 API 服务，将结构化地址（省/市/区/街道/门牌号）解析为对应的位置坐标
        String url = "https://api.map.baidu.com/geocoding/v3/?";
        Map<String, String> params = new HashMap<>();
        params.put("address", address);
        params.put("output", "json");
        params.put("ak", Environment.getBaiduAk());

        JSONObject result = new JSONObject(HttpUtil.get(UrlUtil.buildRequest(url, params))).getJSONObject("result");
        JSONObject location = result.getJSONObject("location");

        return new LocationVO(address, location.getDouble("lat"), location.getDouble("lng"));
    }

    /**
     * 批量算路服务
     */
    @Override
    public Map<Integer, Double> calculate(LocationVO currentLocation, LinkedHashMap<Integer, LocationVO> unArrivedLocations) {
        // 调用百度地图-批量算路 API 服务，获取每组路线的里程
        StringBuilder destinations = new StringBuilder();
        for (Map.Entry<Integer, LocationVO> entry : unArrivedLocations.entrySet()) {
            LocationVO location = entry.getValue();
            destinations.append(location.getLatitude()).append(",").append(location.getLongitude());
            destinations.append("|");
        }

        String url = "https://api.map.baidu.com/routematrix/v2/driving?";
        Map<String, String> params = new HashMap<>();
        params.put("origins", currentLocation.getLatitude() + "," + currentLocation.getLongitude());
        params.put("destinations", destinations.substring(0, destinations.length() - 1));
        params.put("ak", Environment.getBaiduAk());
        params.put("tactics", "12");

        JSONObject response = new JSONObject(HttpUtil.get(UrlUtil.buildRequest(url, params)));
        if (response.length() == 0)
            return null;

        JSONArray result = response.getJSONArray("result");
        Map<Integer, Double> distanceMap = new HashMap<>();
        int i = 0;
        for (Map.Entry<Integer, LocationVO> entry : unArrivedLocations.entrySet()) {
            JSONObject object = result.getJSONObject(i);
            double distance = object.getJSONObject("distance").getDouble("value");
            distanceMap.put(entry.getKey(), distance);
        }

        return distanceMap;
    }
}
