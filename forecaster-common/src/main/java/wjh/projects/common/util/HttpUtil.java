package wjh.projects.common.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;

public class HttpUtil {

    public static String get(String url) {
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("GET");
            try (InputStreamReader inputStreamReader = new InputStreamReader(connection.getInputStream());
                 BufferedReader reader = new BufferedReader(inputStreamReader)) {
                String line;
                StringBuilder sb = new StringBuilder();
                while ((line = reader.readLine()) != null)
                    sb.append(line);

                return sb.toString();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            Objects.requireNonNull(connection).disconnect();
        }
    }
}
