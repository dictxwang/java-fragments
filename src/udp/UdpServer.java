package udp;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UdpServer extends Thread implements UdpCommon {

	private DatagramSocket server = null;
	
	@Override
	public void run() {
		
		 try {
			 server = new DatagramSocket(PORT_SERVER);
			 System.out.println("udp server start...");
			 while (true) {
				 byte[] receive = new byte[1024];
				 DatagramPacket packet = new DatagramPacket(receive, 1024);
				 server.receive(packet);
				 int len = packet.getLength();
				 if (len > 0) {
					 // 处理数据;
					 String msg = new String(receive, 0, len);
					 System.out.println(msg);
					 
					 // 给client写数据
					 int cport = packet.getPort();
					 InetAddress caddress = packet.getAddress();
					 byte[] rbyte = ("response:-" + msg).getBytes();
					 DatagramPacket rpacket = new DatagramPacket(
							 rbyte, rbyte.length, caddress, cport);
					 
					 server.send(rpacket);
				 }
			 }
		 } catch (Exception exp) {
			 System.out.println("server core");
			 exp.printStackTrace();
		 } finally {
			 if (server != null) {
				 server.close();
			 }
		 }
	}
}
