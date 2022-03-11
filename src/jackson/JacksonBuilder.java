package jackson;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

public class JacksonBuilder {

    // 时间精确到毫秒，满足大部分场景
    private static final DateFormat defaultDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    private static final PropertyNamingStrategies.NamingBase defaultNamingBase = new SnakeCaseStrategy();

    private Map<DeserializationFeature, Boolean> descFeatures = null;
    private Map<SerializationFeature, Boolean> serialFeatures = null;
    private PropertyAccessor accessor = null;
    private Visibility visibility = null;
    // 设置输出时包含属性的风格
    private Include inclusion;
    private DateFormat dateFormat;
    // 过滤器
    private FilterProvider filterProvider;
    // 属性命名策略
    private PropertyNamingStrategies.NamingBase namingStrategy;
    // 是否开启属性名驼峰和下划线的转换(默认关闭)
    private boolean useSnakeCase = false;

    public JacksonBuilder() {
    }

    public JacksonBuilder(Include inclusion) {
        this.inclusion = inclusion;
    }

    // 创建输出全部属性到Json字符串的Binder
    public static JacksonBuilder newNormalBuilder() {
        return new JacksonBuilder(Include.ALWAYS);
    }

    // 创建只输出非空属性到Json字符串的Binder
    public static JacksonBuilder newNonNullBuilder() {
        return new JacksonBuilder(Include.NON_NULL);
    }

    // 创建只输出初始值被改变的属性到Json字符串的Binder
    public static JacksonBuilder newNonDefaultBuilder() {
        return new JacksonBuilder(Include.NON_DEFAULT);
    }

    public JacksonBuilder setInclusion(Include inclusion) {
        this.inclusion = inclusion;
        return this;
    }

    public JacksonBuilder setDateFormat(DateFormat dateFormat) {
        this.dateFormat = dateFormat;
        return this;
    }

    public JacksonBuilder setFilterProvider(FilterProvider filterProvider) {
        this.filterProvider = filterProvider;
        return this;
    }

    public JacksonBuilder addSerializationFeature(SerializationFeature feature, boolean value) {
        if (this.serialFeatures == null) {
            this.serialFeatures = new HashMap<>();
        }
        this.serialFeatures.put(feature, value);
        return this;
    }

    public JacksonBuilder addDeserializationFeature(DeserializationFeature feature, boolean value) {
        if (this.descFeatures == null) {
            this.descFeatures = new HashMap<>();
        }
        this.descFeatures.put(feature, value);
        return this;
    }

    public JacksonBuilder setPropertyAccessor(PropertyAccessor accessor, Visibility visibility) {
        this.accessor = accessor;
        this.visibility = visibility;
        return this;
    }

    public JacksonBuilder setPropertyNamingStrategy(PropertyNamingStrategies.NamingBase strategy) {
        this.namingStrategy = strategy;
        return this;
    }

    public JacksonBuilder openUseSnakeCase() {
        this.useSnakeCase = true;
        return this;
    }

    public JacksonBuilder closeUseSnakeCase() {
        this.useSnakeCase = false;
        return this;
    }

    /**
     * 构建ObjectMapper对象
     */
    public ObjectMapper buildObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        if (this.inclusion != null) {
            mapper.setSerializationInclusion(inclusion);
        } else {
            // 设置一个默认值
            mapper.setSerializationInclusion(Include.ALWAYS);
        }
        if (this.dateFormat != null) {
            mapper.setDateFormat(this.dateFormat);
        } else {
            mapper.setDateFormat(defaultDateFormat);
        }
        if (this.filterProvider != null) {
            mapper.setFilterProvider(this.filterProvider);
        }
        if (this.descFeatures != null && !this.descFeatures.isEmpty()) {
            for (Map.Entry<DeserializationFeature, Boolean> entry : this.descFeatures.entrySet()) {
                mapper.configure(entry.getKey(), entry.getValue());
            }
        } else {
            // 默认解析时候忽略json字符串中有但是java对象没有的属性时
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        }
        if (this.serialFeatures != null && !this.serialFeatures.isEmpty()) {
            for (Map.Entry<SerializationFeature, Boolean> entry : this.serialFeatures.entrySet()) {
                mapper.configure(entry.getKey(), entry.getValue());
            }
        }
        if (this.accessor != null && this.visibility != null) {
            mapper.setVisibility(this.accessor, this.visibility);
        } else {
            // 默认设置所有属性均可见
            mapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
        }
        if (this.namingStrategy != null) {
            mapper.setPropertyNamingStrategy(this.namingStrategy);
        } else if (this.useSnakeCase) {
            // 默认进行驼峰和小写下划线的转换
            mapper.setPropertyNamingStrategy(defaultNamingBase);
        }
        return mapper;
    }
}
