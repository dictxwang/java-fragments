package cpython;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

public class CallPythonServer {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		try {
			//InetAddress addr = Inet4Address.getByName("localhost");
			
			int port = 54321;
			//InetSocketAddress addr = new InetSocketAddress("192.168.3.103", port);
			Socket socket = new Socket("192.168.3.103", port);
			socket.setKeepAlive(true);
			OutputStream outstream = socket.getOutputStream();
			outstream.write("wangqiang".getBytes("utf-8"));
			InputStream instream = socket.getInputStream();
			byte[] out = new byte[1024];
			instream.read(out);

			String result = new String(out, 0, out.length);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}