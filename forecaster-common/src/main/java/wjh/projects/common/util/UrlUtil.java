package wjh.projects.common.util;

import java.util.Map;

public class UrlUtil {

    public static String buildRequest(String url, Map<String, String> params) {
        if (url == null || url.length() == 0 || params == null || params.size() == 0)
            throw new IllegalArgumentException("输入的 url 或参数为空");

        StringBuilder sb = new StringBuilder(url);
        for (Map.Entry<String, String> param : params.entrySet()) {
            sb.append(param.getKey()).append("=");
            sb.append(param.getValue()).append("&");
        }

        return sb.deleteCharAt(sb.length() - 1).toString();
    }
}
