package socket;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Random;
import java.util.UUID;


public class Client implements Runnable {

	private Socket socket;
	private BufferedReader reader;
	private PrintWriter writer;
	private int count = 0;
	private String name;
	public Client(String name) {
		this.name = name;
	}

	public void run() {
		while (count++ < 10) {
			try {
			    socket = new Socket("127.0.0.1", 10001);
			    reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			    writer = new PrintWriter(socket.getOutputStream());
			    String info = UUID.randomUUID().toString();
			    writer.println("["+ this.name +"]-" + info);
			    writer.flush();

			    String line = null;
			    while ((line = reader.readLine()) != null) {
			    	System.out.println(line);
			    	break;
			    }
			    
			    writer.close();
			    reader.close();
			    socket.close();
			    
			    Thread.sleep((long)(new Random().nextDouble() * 3000));
			} catch (Exception exp) {
				exp.printStackTrace();
			}
		}
	}
}
