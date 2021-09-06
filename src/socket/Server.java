package socket;

import java.net.ServerSocket;
import java.net.Socket;

public class Server implements Runnable {

	private ServerSocket ss;
	private Socket socket;
	private int count = 0;
	public Server() {
		
	}
	public void run() {
		try {
			ss = new ServerSocket(10001);
			while (count++ < 10) {
				socket = ss.accept();
				ThreadServer ts = new ThreadServer(socket);
				ts.start();
			}
		} catch (Exception exp) {
			exp.printStackTrace();
		}
	}
}
