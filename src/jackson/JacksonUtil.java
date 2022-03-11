package jackson;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JacksonUtil {

    public static boolean maybeJsonString(String jsonString) {
        if (jsonString == null || jsonString.isEmpty()) {
            return false;
        } else {
            jsonString = jsonString.trim();
            return "null".equals(jsonString)
                    || "Null".equals(jsonString)
                    || jsonString.startsWith("[") && jsonString.endsWith("]")
                    || jsonString.startsWith("{") && jsonString.endsWith("}");
        }
    }

    /**
     * 判断是否是非空的json字符串
     *
     * @param jsonString json字符串
     * @return boolean
     * @throws Exception
     */
    public static boolean isNotEmptyJson(String jsonString) throws Exception {
        if (maybeJsonString(jsonString)) {
            jsonString = jsonString.trim();
            ObjectMapper mapper = new ObjectMapper();
            return !mapper.readTree(jsonString).isEmpty();
        }
        return false;
    }
}
