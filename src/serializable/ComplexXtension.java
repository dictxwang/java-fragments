package serializable;

import java.util.List;

@SuppressWarnings("serial")
public class ComplexXtension extends Complex {

	transient private int id;
	private String type;
	private List<String> value;
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public List<String> getValue() {
		return value;
	}
	public void setValue(List<String> value) {
		this.value = value;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
}
