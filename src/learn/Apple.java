package learn;

public class Apple implements Fruit {
	
	private String name;
	private String color;

	public Apple(String name, String color) {
		this.name = name;
		this.color = color;
	}
	
	@Override
	public String getName() {
		return "apple name - " + this.name;
	}
	
	@Override
	public String getColor() {
		return "apple color - " + this.color;
	}
}
