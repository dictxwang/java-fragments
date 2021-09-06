package cpython;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Random;
import java.util.UUID;

public class CallPythonServerWithNio {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		String hostname = "192.168.3.103";
		int port = 54321;
		CallPythonClient client01 = new CallPythonClient("001", hostname, port);
		CallPythonClient client02 = new CallPythonClient("002", hostname, port);
		CallPythonClient client03 = new CallPythonClient("003", hostname, port);
		CallPythonClient client04 = new CallPythonClient("004", hostname, port);
		
		client01.start();
		client02.start();
		client03.start();
		client04.start();
	}
}

class CallPythonClient extends Thread {
	String name;
	String hostname;
	int port;
	private int count = 0;
	private Charset charset = Charset.forName("UTF-8");
	
	public CallPythonClient(String name, String hostname, int port) {
		this.name = name;
		this.hostname = hostname;
		this.port = port;
	}
	
	@Override
	public void run() {
		
		while (count++ < 10) {
			SocketChannel channel = null;
			try {
				channel = SocketChannel.open();
				InetSocketAddress addr = new InetSocketAddress(hostname, port);
				channel.connect(addr);
				ByteBuffer buffer = ByteBuffer.allocate(1024);
				buffer.clear();
				String msg = UUID.randomUUID().toString();
				buffer.put(charset.encode(msg));
				buffer.put(charset.encode("-[nio client "+ name +"]"));
				buffer.flip();
				channel.write(buffer);
				
				buffer.clear();
				channel.read(buffer);
				buffer.flip();
				System.out.println(charset.decode(buffer));
				
				Thread.sleep(new Random().nextInt(2000));
			} catch (Exception exp) {
				exp.printStackTrace();
			} finally {
				try {
					channel.close();
				} catch (Exception exp) {
					exp.printStackTrace();
				}
			}
		}
	}
}