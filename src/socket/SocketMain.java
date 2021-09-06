package socket;

public class SocketMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Server s = new Server();
		Client c = new Client("c1");
		Client c2 = new Client("c2");
		Client c3 = new Client("c3");
		
		Thread sT = new Thread(s);
		Thread cT = new Thread(c);
		Thread cT2 = new Thread(c2);
		Thread cT3 = new Thread(c3);
		
		cT.start();
		sT.start();
		cT2.start();
		cT3.start();
	}

}
