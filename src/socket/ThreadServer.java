package socket;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class ThreadServer extends Thread {

	private Socket s = null;
	private BufferedReader r = null;
	private BufferedWriter w = null;
	
	public ThreadServer(Socket s) {
		this.s = s;
	}
	
	public void run() {
		try {
			r = new BufferedReader(new InputStreamReader(s.getInputStream()));
			w = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
			String line = null;
			while ((line = r.readLine()) != null) {
//				System.out.println(line);
				w.write("server response:" + line);
				w.flush();
				break;
			}
		} catch (IOException exp) {
			exp.printStackTrace();
		} finally {
			if (r != null) {
				try {
				    r.close();
				} catch (Exception exp) {}
			}
			if (s != null) {
				try {
					s.close();
				} catch (Exception exp) {}
			}
			if (w != null) {
				
			}
		}
	}
}
