package jackson;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.text.SimpleDateFormat;

public class JacksonFastWorker {

    private static JacksonFastWorker instance;
    private ObjectMapper mapper;

    private JacksonFastWorker() {
        mapper = new ObjectMapper();
        // 采用默认的配置
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        // 将空字符当做null处理
        // mapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
        // 设置输出时包含属性的风格
        mapper.setSerializationInclusion(Include.NON_NULL);
        // 可以序列化对象的所有访问属性的成员
        mapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
        // 设置时间转换规则
        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS"));
    }

    public static JacksonFastWorker getInstance() {
        if (instance == null) {
            synchronized (JacksonFastWorker.class) {
                if (instance == null) {
                    instance = new JacksonFastWorker();
                }
            }
        }
        return instance;
    }

    /**
     * 如果对象为Null, 返回"null".
     * 如果集合为空集合, 返回"[]".
     */
    public String toJsonString(Object obj) {
        try {
            return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException exp) {
        }
        return null;
    }

    /**
     * 如果JSON字符串为Null或"null"字符串, 返回Null.<br/>
     * 如果JSON字符串为"[]", 返回空集合.<br/>
     * Note: 只能处理非集合的普通类型<br/>
     *
     * @param jsonString json字符串
     * @param clazz 普通Object类型
     * @param <T> 类型
     * @return 反序列化之后的普通类型
     */
    public <T> T fromJsonString(String jsonString, Class<T> clazz) {
        if (jsonString == null || jsonString.isEmpty()) {
            return null;
        }
        try {
            return mapper.readValue(jsonString, clazz);
        } catch (IOException exp) {
        	exp.printStackTrace();
        }
        return null;
    }

    /**
     * 复杂的list或者map类型，需要用TypeReference来指明具体的类型，否则类型擦除会导致问题
     *
     * @param jsonString json字符串
     * @param tr TypeReference
     * @return list或者map等复杂类型
     */
    public <T> T fromJsonString(String jsonString, TypeReference<T> tr) {
        if (jsonString == null || jsonString.isEmpty()) {
            return null;
        }

        try {
            return mapper.readValue(jsonString, tr);
        } catch (IOException exp) {
        }
        return null;
    }
}
