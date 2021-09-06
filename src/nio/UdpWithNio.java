package nio;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Random;
import java.util.UUID;

public class UdpWithNio {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		UdpServer server = new UdpServer(5511);
		server.start();
		
		UdpClient client1 = new UdpClient("uc1", 5511);
		UdpClient client2 = new UdpClient("uc2", 5511);
		UdpClient client3 = new UdpClient("uc3", 5511);
		client1.start();
		client2.start();
		client3.start();

		UdpNioClient client11 = new UdpNioClient("uic1", 5511);
		UdpNioClient client12 = new UdpNioClient("uic2", 5511);
		UdpNioClient client13 = new UdpNioClient("uic3", 5511);
		client11.start();
		client12.start();
		client13.start();
	}
}

class UdpNioClient extends Thread {
	private String name = "";
	private int serverport;
	private Charset charset = Charset.forName("UTF-8");
	
	public UdpNioClient(String name, int serverport) {
		this.name = name;
		this.serverport = serverport;
	}
	
	@Override
	public void run() {
		InetSocketAddress serveraddress = new InetSocketAddress("localhost", this.serverport);
		int count = 0;
		while (count++ < 10000) {
			DatagramChannel channel = null;
			try {
				channel = DatagramChannel.open();
				ByteBuffer buffer = ByteBuffer.allocate(512);
				String request = UUID.randomUUID().toString().replace("-", "_");
				request = request + "-["+ this.name +"]-[Nio]";
				buffer = charset.encode(request);
				channel.send(buffer, serveraddress);
				
				buffer.clear();
				while (channel.receive(buffer) == null);
				buffer.flip();
				String response = charset.decode(buffer).toString();
				System.out.println("WRITE:" + response + "["+ this.name +"]-[Nio]");
				
				sleep(1000);
			} catch (Exception exp) {
				exp.printStackTrace();
			} finally {
				try {
					channel.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}

class UdpClient extends Thread {
	private String name = "";
	private int serverport;
	
	public UdpClient(String name, int serverport) {
		this.name = name;
		this.serverport = serverport;
	}
	
	@Override
	public void run() {
		int count = 0;
		try {
			while (count++ <= 10000) {
				DatagramSocket socket = new DatagramSocket();
				byte[] data = new byte[1024];
				String msg = UUID.randomUUID().toString().replace("-", "_");
				data = (msg + "-["+ this.name +"]-[Normal]").getBytes();
				
				DatagramPacket packet =new DatagramPacket(data, data.length,
						InetAddress.getByName("127.0.0.1"), this.serverport);
				socket.send(packet);
				
				while (true) {
					// 接受数据
					byte[] rdata = new byte[1024];
					DatagramPacket rpacket = new DatagramPacket(rdata, 1024);
					socket.receive(rpacket);
					int len = rpacket.getLength();
					if (len > 0) {
						String r = new String(rdata, 0, len);
						System.out.println("WRITE:" + r + "-["+ this.name +"]-[Normal]");
						break;
					}
				}
				socket.close();
				
				Thread.sleep(new Random().nextInt(1000) + 600);
			}
		} catch (Exception exp) {
			System.out.println("[Client:"+ this.name +"] core");
			exp.printStackTrace();
		}
	}
}

class UdpServer extends Thread {
	
	private DatagramChannel channel = null;
	private Selector selector = null;
	private int port;
	private Charset charset = Charset.forName("UTF-8");
	
	public UdpServer(int port) {
		this.port = port;
	}
	
	@Override
	public void run() {
		
		try {
			channel = DatagramChannel.open();
			selector = Selector.open();
			InetSocketAddress address = new InetSocketAddress("127.0.0.1", port);
			channel.socket().bind(address);
			channel.configureBlocking(false);
			channel.register(selector, SelectionKey.OP_READ);
			System.out.println("Udp server starting...");
			
			while (true) {
				if (selector.select() < 1) {
					continue;
				}
				Iterator<SelectionKey> keys = selector.selectedKeys().iterator();
				while (keys.hasNext()) {
					SelectionKey selectionKey = keys.next();
					keys.remove();
					
					if (!selectionKey.isValid()) {
						continue;
					}
					
					if (selectionKey.isReadable()) {
						doRead(selectionKey);
					}
				}
			}
		} catch (Exception exp) {
			exp.printStackTrace();
		}
	}
	
	void doRead(SelectionKey selectionKey) {
		DatagramChannel channel = (DatagramChannel)selectionKey.channel();
		while (true) {
			ByteBuffer buffer = ByteBuffer.allocate(512);
			try {
				SocketAddress address;
				while ((address = channel.receive(buffer)) == null);
				buffer.flip();
				String request = charset.decode(buffer).toString();
				System.out.println("READ:" + request);

				String response = request.split("-")[0];
				buffer.clear();
				buffer = this.charset.encode(response);
				channel.send(buffer, address);
				return;
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
//				try {
//					channel.close();
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
			}
		}
	}
}