package rmi;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Fruit implements Serializable {

	private String name = "";
	private String color = "";
	
	public Fruit() {}
	
	public Fruit(String name, String color) {
		this.name = name;
		this.color = color;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Fruit [name=");
		builder.append(name);
		builder.append(", color=");
		builder.append(color);
		builder.append("]");
		return builder.toString();
	}
}
