package serializable;

import java.io.Serializable;
import java.util.Map;

@SuppressWarnings("serial")
public class Complex implements Serializable {

	private String name;
	private Map<String, String> map;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Map<String, String> getMap() {
		return map;
	}
	public void setMap(Map<String, String> map) {
		this.map = map;
	}
}
