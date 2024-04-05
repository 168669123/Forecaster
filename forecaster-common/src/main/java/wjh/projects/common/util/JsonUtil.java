package wjh.projects.common.util;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.Locale;

public class JsonUtil {
    private static final Gson gson;

    static {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Date.class, new DateDeserializer());
        gsonBuilder.registerTypeAdapter(Date.class, new DateSerializer());
        gson = gsonBuilder.create();
    }

    static class DateDeserializer implements JsonDeserializer<Date> {
        @Override
        public Date deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            Date date;
            String dateString = jsonElement.getAsJsonPrimitive().getAsString();
            date = DateUtil.stringToDate(dateString, "yyyy-MM-dd HH:mm:ss");
            date = date == null ? DateUtil.stringToDate(dateString, "EEE MMM dd HH:mm:ss Z yyyy", Locale.ENGLISH) : date;
            return date;
        }
    }

    static class DateSerializer implements JsonSerializer<Date> {
        @Override
        public JsonElement serialize(Date date, Type type, JsonSerializationContext jsonSerializationContext) {
            return new JsonPrimitive(DateUtil.dateToString(date, "yyyy-MM-dd HH:mm:ss"));
        }
    }

    public static String toJson(Object object) {
        return gson.toJson(object);
    }

    public static <T> T parseJson(String json, Class<T> clazz) {
        return gson.fromJson(json, clazz);
    }
}
