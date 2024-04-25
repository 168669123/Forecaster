package wjh.projects.infrastructure.rpc.impl;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import wjh.projects.common.constants.PropertiesEnum;
import wjh.projects.common.util.HttpUtil;
import wjh.projects.common.util.PropertiesUtil;
import wjh.projects.common.util.UrlUtil;
import wjh.projects.domain.transportVehicle.model.entity.Site;
import wjh.projects.domain.transportVehicle.model.vo.LocationVO;
import wjh.projects.infrastructure.rpc.FacadeClient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FacadeClientImpl implements FacadeClient {

    /**
     * 百度地图-地理编码服务，将结构化地址（省/市/区/街道/门牌号）解析为对应的位置坐标
     */
    public LocationVO geocode(String address) {
        String url = "https://api.map.baidu.com/geocoding/v3/?";
        Map<String, String> params = new HashMap<>();
        params.put("address", address);
        params.put("output", "json");
        params.put("ak", PropertiesUtil.get(PropertiesEnum.BAIDU_AK));

        JSONObject result = new JSONObject(HttpUtil.get(UrlUtil.buildRequest(url, params))).getJSONObject("result");
        JSONObject location = result.getJSONObject("location");
        return new LocationVO(address, location.getDouble("lat"), location.getDouble("lng"));
    }

    /**
     * 百度地图-批量算路服务，获取每组路线的里程
     *
     * @param current 当前位置
     * @param sites   目标站点集合
     */
    @Override
    public Map<Site, Double> calculateMileages(LocationVO current, List<Site> sites) {
        StringBuilder destinations = new StringBuilder();
        for (Site site : sites) {
            LocationVO locationVO = site.getSiteIdVO().getLocationVO();
            destinations.append(locationVO.getLatitude()).append(",").append(locationVO.getLongitude());
            destinations.append("|");
        }

        String url = "https://api.map.baidu.com/routematrix/v2/driving?";
        Map<String, String> params = new HashMap<>();
        params.put("origins", current.getLatitude() + "," + current.getLongitude());
        params.put("destinations", destinations.substring(0, destinations.length() - 1));
        params.put("ak", PropertiesUtil.get(PropertiesEnum.BAIDU_AK));
        params.put("tactics", "12");

        JSONObject response = new JSONObject(HttpUtil.get(UrlUtil.buildRequest(url, params)));
        JSONArray result = response.getJSONArray("result");
        if (result != null && result.length() == sites.size()) {
            Map<Site, Double> mileages = new HashMap<>();
            for (int i = 0; i < sites.size(); i++) {
                JSONObject object = result.getJSONObject(i);
                mileages.put(sites.get(i), object.getJSONObject("distance").getDouble("value"));
            }
            return mileages;
        }
        throw new RuntimeException("百度地图-批量算路服务错误");
    }
}
