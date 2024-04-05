package wjh.projects.common.util;

import com.alibaba.fastjson2.JSON;
import javafx.util.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.util.*;
import java.util.regex.Pattern;

public class StringUtil {
    /**
     * 用于判断 value 的数据类型
     */
    public static Pattern numPattern = Pattern.compile("^-?[0-9]+\\.?[0-9]*$");
    public static Pattern objectPattern = Pattern.compile("^[a-zA-Z0-9\\.]+\\(.+\\)$");
    public static Pattern listPattern = Pattern.compile("^\\[.*\\]$");
    public static Pattern mapPattern = Pattern.compile("^\\{.*\\}$");
    public static Pattern supperPattern = Pattern.compile("^super=[a-zA-Z0-9\\.]+\\(.+\\)$");
    public static final String NULL = "null";
    private final static Map<Character, Character> tokenMap = new HashMap<>();

    static {
        tokenMap.put(')', '(');
        tokenMap.put('}', '{');
        tokenMap.put(']', '[');
    }

    /**
     * 将 toString 字符串转换为 json 字符串
     *
     * @return 如果转换失败，则直接返回原字符串
     */
    public static String toJSONString(String toString) {
        try {
            return JSON.toJSONString(toMap(toString));
        } catch (Exception e) {
            return toString;
        }
    }

    /**
     * 将 toString 字符串转换成键值对结构
     */
    private static Map<String, Object> toMap(String toString) throws ParseException {
        if (isEmpty(toString))
            return null;

        // 移除 toString 字符串最外层 "()"
        toString = toString.substring(toString.indexOf("(") + 1);
        toString = toString.substring(0, toString.lastIndexOf(")"));

        Map<String, Object> map = new HashMap<>();
        while (!isEmpty(toString)) {
            String token = getFirstToken(toString);
            if (isEmpty(token))
                break;

            toString = removeStart(removeStart(toString, token).trim(), ",").trim();
            // 如果存在 "super=" (通过 lombok 的 @ToString(callSuper=true) 引入)，则按照当前层继续处理
            if (supperPattern.matcher(token).matches()) {
                token = token.substring(token.indexOf("(") + 1, token.length() - 1);
                toString = String.format("%s,%s", token, toString);
                continue;
            }

            Pair<String, String> keyValue = parseToken(token);
            map.put(keyValue.getKey(), buildTypeValue(keyValue.getValue()));
        }
        return map;
    }

    /**
     * 从已经移除了最外层 "()" 的 toString 字符串中，获取其第一个 token
     * token 格式为 key=value
     */
    static String getFirstToken(String toString) {
        if (isBlank(toString))
            return toString;

        int bracketNum = 0;
        Stack<Character> stack = new Stack<>();
        for (int i = 0; i < toString.length(); i++) {
            Character c = toString.charAt(i);
            if (tokenMap.containsValue(c)) {
                stack.push(c);
            } else if (tokenMap.containsKey(c) && Objects.equals(stack.peek(), tokenMap.get(c))) {
                stack.pop();
            } else if ((c == ',') && stack.isEmpty()) {
                return toString.substring(0, i);
            }
        }
        if (stack.isEmpty())
            return toString;

        throw new RuntimeException("获取 token 失败，bracketNum=" + bracketNum + "，toString=" + toString);
    }

    /**
     * 根据分隔符 "=" 将 token 转换为 key 和 value
     */
    static Pair<String, String> parseToken(String token) {
        if (isEmpty(token) || !token.contains("="))
            throw new RuntimeException("token 为空或其中不含\"=\"，token：" + token);

        int index = token.indexOf("=");
        return new javafx.util.Pair<>(token.substring(0, index), token.substring(index + 1));
    }

    /**
     * 将 value 转换成其对应的数据类型
     */
    private static Object buildTypeValue(String value) throws ParseException {
        if (isEmpty(value) || value.equals(NULL))
            return null;

        // 数字类型
        if (numPattern.matcher(value).matches())
            return value;

        // 对象类型
        if (objectPattern.matcher(value).matches())
            return toMap(value);

        // 集合类型
        if (listPattern.matcher(value).matches())
            return buildListValue(value);

        // Map 类型
        if (mapPattern.matcher(value).matches())
            return buildMapValue(value);

        // 其它的都认为是 String 类型
        return value;
    }

    /**
     * 集合类型
     */
    private static Object buildListValue(String value) throws ParseException {
        List<Object> result = new ArrayList<>();
        value = value.substring(1, value.length() - 1).trim();
        if (isEmpty(value))
            return result;

        while (!isEmpty(value)) {
            String token = getFirstToken(value);
            if (isEmpty(token))
                break;

            result.add(buildTypeValue(token));
            value = removeStart(removeStart(value, token).trim(), ",").trim();
        }
        return result;
    }

    /**
     * Map 类型
     */
    private static Map<Object, Object> buildMapValue(String value) throws ParseException {
        Map<Object, Object> result = new HashMap<>();
        value = value.substring(1, value.length() - 1).trim();
        if (isEmpty(value))
            return result;

        while (!isEmpty(value)) {
            String token = getFirstToken(value);
            if (isEmpty(token))
                break;

            Pair<String, String> keyValue = parseToken(token);
            result.put(buildTypeValue(keyValue.getKey()), buildTypeValue(keyValue.getValue()));
            value = removeStart(removeStart(value, token).trim(), ",").trim();
        }
        return result;
    }

    /**
     * 判断字符串是否为 null 或长度为0
     */
    public static boolean isEmpty(String string) {
        return string == null || string.length() == 0;
    }

    /**
     * 判断字符串是否wei null 或长度为0或由空白符构成
     */
    public static boolean isBlank(String string) {
        if (isEmpty(string))
            return true;

        for (int i = 0; i < string.length(); i++) {
            if (!Character.isWhitespace(string.charAt(i)))
                return false;
        }
        return true;
    }

    /**
     * 如果 string 以 remove 开头，则从 string 中删除 remove，否则返回 string 原字符串
     */
    public static String removeStart(String string, String remove) {
        if (isEmpty(string) || isEmpty(remove))
            return string;

        if (string.startsWith(remove))
            return string.substring(remove.length());

        return string;
    }
}
