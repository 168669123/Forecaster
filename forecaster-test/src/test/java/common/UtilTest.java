package common;

import com.google.gson.JsonPrimitive;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import wjh.projects.common.Environment;
import wjh.projects.common.util.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class UtilTest {

    @Test
    public void testCalculateLinearDistance() {
        double linearDistance = GeoUtil.calculateLinearDistance(
                121.617834,
                120.379129,
                31.037175,
                30.335241);
        Assertions.assertEquals(linearDistance, 141852.7);
    }

    @Test
    public void testParseJsonWithDate() {
        JsonUtil.DateDeserializer dateDeserializer = new JsonUtil.DateDeserializer();
        String json1 = "2024-03-06 06:26:54";
        String json2 = "Wed Mar 06 06:26:54 GMT+08:00 2024";
        Date date1 = dateDeserializer.deserialize(new JsonPrimitive(json1), null, null);
        Date date2 = dateDeserializer.deserialize(new JsonPrimitive(json2), null, null);
        assert date1.getTime() == date2.getTime();
    }

    @Test
    public void testAddSeconds() {
        Date date1 = new Date();
        Date date2 = DateUtil.addSeconds(date1, 1000);
        Assertions.assertEquals(date1.getTime() + 1000 * 1000, date2.getTime());
    }

    @Test
    public void testToJSONString() {
        String toString = "object(id=test, data=info)";
        String jsonString = StringUtil.toJSONString(toString);
        System.out.println(jsonString);
        Assertions.assertEquals(jsonString, "{\"data\":\"info\",\"id\":\"test\"}");
    }

    @Test
    public void testBuildRequest() {
        String url = "https://route?";
        Map<String, String> params = new HashMap<>();
        params.put("param1", "test1");
        String request = UrlUtil.buildRequest(url, params);
        System.out.println(request);
    }
}
