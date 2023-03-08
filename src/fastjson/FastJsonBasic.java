package fastjson;

import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

public class FastJsonBasic {

	public static void main(String[] args) {
		
		String json = "{ \"1001197\": { \"3\":0, \"5\":0, \"6\":0 } }";
		
		// 以下两种解析方式均可
		Map<String, Map<String, Long>> mapA = JSON.parseObject(json, new TypeReference<Map<String, Map<String, Long>>>(){});
		Map<String, Map<String, Long>> mapB = JSON.parseObject(json, Map.class);
		
		System.out.println(mapA.get("1001197").get("3"));
		System.out.println(mapB.get("1001197").get("5"));
	}

}
