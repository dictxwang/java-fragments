package nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Random;

public class SocketSelectorMultiplexer {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			Selector selector = Selector.open();
			NioMultiplexerExecutor executor = new NioMultiplexerExecutor(selector);
			String host = "192.168.3.103";
			int portA = 10002, portB = 10003;
			NioMultiplexerServer serverA = new NioMultiplexerServer(host, portA, selector);
			NioMultiplexerServer serverB = new NioMultiplexerServer(host, portB, selector);
			
//			serverA.start();
//			serverB.start();
//			executor.start();
			for (int i = 1; i < 50; i++) {
				NioClient nioClient = new NioClient("CA" + i, host, portA);
				NormalClient normalClient = new NormalClient("CB" + i, host, portB);
				nioClient.start();
				normalClient.start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}

class NioMultiplexerExecutor extends Thread {
	
	private final Selector selector;
	private final Charset charset = Charset.forName("UTF-8");
	
	public NioMultiplexerExecutor(Selector selector) {
		this.selector = selector;
	}
	
	@Override
	public void run() {

		try {
			System.out.println("multiplexer start...");
			sleep(2000);
			while (true) {
				if (selector.select() < 1) {
					continue;
				}
				Iterator<SelectionKey> selectionkeys = selector.selectedKeys().iterator();
				while (selectionkeys.hasNext()) {
					SelectionKey selectionkey = selectionkeys.next();
					selectionkeys.remove();
					
					if (!selectionkey.isValid()) {
						continue;
					}
					if (selectionkey.isAcceptable()) {
						doAccept(selectionkey);
					} else if (selectionkey.isReadable()) {
						doRead(selectionkey);
					} else if (selectionkey.isWritable()) {
						doWrite(selectionkey);
					}
				}
			}
		} catch (Exception exp) {
			exp.printStackTrace();
		}
	}

	void doAccept(SelectionKey key) {
		try {
			ServerSocketChannel serverchannel = (ServerSocketChannel)key.channel();
			SocketChannel socketchannel = serverchannel.accept();
			if (socketchannel != null) {
				socketchannel.configureBlocking(false);
				socketchannel.register(key.selector(), SelectionKey.OP_READ);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	void doRead(SelectionKey key) {
		SocketChannel keychannel = (SocketChannel)key.channel();
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		int len = -1;
		try {
			len = keychannel.read(buffer);
		} catch (IOException e) {
			e.printStackTrace();
			try {
				keychannel.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			key.cancel();
			return;
		}
		if (len != -1) {
			String input = new String(buffer.array()).trim();
			System.out.println("READ:" + input);
			try {
				keychannel.configureBlocking(false);
				keychannel.register(key.selector(), SelectionKey.OP_WRITE);
				key.attach("WRITE:" + input);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			try {
				keychannel.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			key.cancel();
			return;
		}
	}
	
	void doWrite(SelectionKey key) {
		SocketChannel channel = (SocketChannel)key.channel();
		try {
			String out = (String)key.attachment();
			Thread.sleep(Random.class.newInstance().nextInt(10));
			channel.write(charset.encode(out));
			channel.register(key.selector(), SelectionKey.OP_READ);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				channel.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			key.cancel();
		}
	}
}

class NioMultiplexerServer extends Thread {

	private ServerSocketChannel serverchannel;
	private Selector selector;
	private String host;
	private int port = 11204;
	
	public NioMultiplexerServer(String host, int port, Selector selector) {
		this.host = host;
		this.port = port;
		this.selector = selector;
	}

	@Override
	public void run() {
		try {
			serverchannel = ServerSocketChannel.open();
			InetSocketAddress isa = new InetSocketAddress(host, port);
			serverchannel.configureBlocking(false);
			serverchannel.socket().bind(isa);
			serverchannel.register(selector, SelectionKey.OP_ACCEPT);
			System.out.println("server start on " + this.port);

		} catch (Exception exp) {
			exp.printStackTrace();
		}
	}
}