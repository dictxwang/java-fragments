package learn;

public class Pear implements Fruit {

    String name;
	private String color;
	
	public Pear(String name, String color) {
		this.name = name;
		this.color = color;
	}
	
	@Override
	public String getName() {
		return "pear name - " + this.name;
	}
	
	@Override
	public String getColor() {
		return "pear color - " + this.color;
	}
	
	public String getOwner() {
		return "this is pear";
	}
}
class P {
	public void print() {
		System.out.println("inner method");
	}
}