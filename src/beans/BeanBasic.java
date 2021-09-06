package beans;

import java.beans.Introspector;

public class BeanBasic {

//	Introspector 
}

class Car {
	
	private String color;
	private int speed;
	
	public Car(String color, int speed) {
		super();
		this.color = color;
		this.speed = speed;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}
}