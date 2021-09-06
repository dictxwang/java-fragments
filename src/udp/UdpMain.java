package udp;

public class UdpMain implements UdpCommon {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		UdpServer server = new UdpServer();
		server.start();
		
		UdpClient client1 = new UdpClient("c001");
		UdpClient client2 = new UdpClient("c002");
		client1.start();
		client2.start();
	}

}
