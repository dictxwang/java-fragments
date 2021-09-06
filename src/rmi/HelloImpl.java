package rmi;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

@SuppressWarnings("serial")
public class HelloImpl extends UnicastRemoteObject implements IHello {

	public HelloImpl() throws RemoteException {}

	@Override
	public String helloWorld() throws RemoteException {
		return "Hello, World";
	}
	
	@Override
	public String sayHelloToSomeBody(String somebody) throws RemoteException {
		return "Hello, " + somebody;
	}
	
	@Override
	public Fruit getFruit(String name, String color) throws RemoteException {
		return new Fruit(name, color);
	}
}
