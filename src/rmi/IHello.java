package rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IHello extends Remote {

	public String helloWorld() throws RemoteException;
	public String sayHelloToSomeBody(String somebody) throws RemoteException;
	public Fruit getFruit(String name, String color) throws RemoteException;
}
