package udp;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Random;
import java.util.UUID;

public class UdpClient extends Thread implements UdpCommon {

	private String name = "";
	
	public UdpClient(String name) {
		this.name = name;
	}

	@Override
	public void run() {
		int count = 1;
		try {
			while (count++ <= 10) {
				DatagramSocket socket = new DatagramSocket();
				byte[] data = new byte[1024];
				String msg = UUID.randomUUID().toString();
				data = ("["+ this.name +"]=" + msg).getBytes();
				
				DatagramPacket packet =new DatagramPacket(data, data.length,
						InetAddress.getByName("127.0.0.1"), PORT_SERVER);
				socket.send(packet);
				
				while (true) {
					// 接受数据
					byte[] rdata = new byte[1024];
					DatagramPacket rpacket = new DatagramPacket(rdata, 1024);
					socket.receive(rpacket);
					int len = rpacket.getLength();
					if (len > 0) {
						String r = new String(rdata, 0, len);
						System.out.println(r);
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
