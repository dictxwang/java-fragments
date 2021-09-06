package nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

public class CharsetInNio {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		baiduReader();
	}

	static void baiduReader() {
		
		Charset charset = Charset.forName("GBK");
		SocketChannel schannel = null;
		
		try {
			InetSocketAddress saddress = new InetSocketAddress("www.baidu.com", 80);
			schannel = SocketChannel.open(saddress);
			
			schannel.write(charset.encode("GET " + "/ HTTP/1.1\r\n\r\n"));
			
			ByteBuffer buffer = ByteBuffer.allocate(1024);
			while (schannel.read(buffer) != -1) {
				buffer.flip();
				System.out.println(charset.decode(buffer));
				buffer.clear();
			}
		} catch (Exception exp) {
			if (schannel != null) {
				try {
					schannel.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
