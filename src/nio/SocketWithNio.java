package nio;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Random;
import java.util.UUID;

public class SocketWithNio {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
//		useNormalClient();
//		useNioClient();
		useMixClient();
//		useMultiServer();
	}
	
	static void useMultiServer() {
		Selector selector;
		try {
			selector = Selector.open();
			int portA = 11204, portB = 11205;
			NioServer serverA = new NioServer("multi-A", portA, selector);
			NioServer serverB = new NioServer("multi-B", portB, selector);
			NioClient nioClient = new NioClient(portA);
			NormalClient normalClient = new NormalClient(portB);
			
			serverA.start();
			serverB.start();
			nioClient.start();
			normalClient.start();
		} catch (Exception exp) {
			exp.printStackTrace();
		}
	}
	
	static void useMixClient() {
		Selector selector;
		try {
			selector = Selector.open();
			int port = 11204;
			NioServer server = new NioServer("normal", port, selector);
			NioClient nioclient = new NioClient(port);
			NioClient nioclient2 = new NioClient(port);
			NioClient nioclient3 = new NioClient(port);
			NormalClient normalclient = new NormalClient(port);
			NormalClient normalclient2 = new NormalClient(port);
			NormalClient normalclient3 = new NormalClient(port);

			server.start();
			nioclient.start();
			nioclient2.start();
			nioclient3.start();

			normalclient.start();
			normalclient2.start();
			normalclient3.start();
		} catch (Exception exp) {
			exp.printStackTrace();
		}
		
	}
	
	static void useNioClient() {
		Selector selector;
		try {
			selector = Selector.open();
			int port = 11204;
			NioServer server = new NioServer("normal", port, selector);
			NioClient client = new NioClient(port);
			NioClient client2 = new NioClient(port);
			NioClient client3 = new NioClient(port);
			server.start();
			client.start();
			client2.start();
			client3.start();
		} catch (Exception exp) {
			exp.printStackTrace();
		}
	}
	
	static void useNormalClient() {
		Selector selector;
		try {
			selector = Selector.open();
			int port = 11204;
			NioServer server = new NioServer("normal", port, selector);
			NormalClient client = new NormalClient(port);
			NormalClient client2 = new NormalClient(port);
			NormalClient client3 = new NormalClient(port);
			server.start();
			client.start();
			client2.start();
			client3.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

class NormalClient extends Thread {
	private Socket socket;
	private String host = "127.0.0.1";
	private int port = 11204;
	BufferedWriter writer = null;
	BufferedReader reader = null;
	String name;
	
	public NormalClient(int port) {
		this.port = port;
	}
	
	public NormalClient(String name, int port) {
		this.name = name;
		this.port = port;
	}
	
	public NormalClient(String name, String host, int port) {
		this(name, port);
		this.host = host;
	}
	
	@Override
	public void run() {
		int count = 20000;
		while (count-- > 0) {
			try {
				socket = new Socket(host, port);
				socket.setSoTimeout(10000);
				
				writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
				reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				
				String msg;
				if (this.name == null) {
					msg = "["+ count +"]-";
				} else {
					msg = "[" + this.name + "]-[" + count + "]-";
				}
				msg = UUID.randomUUID().toString().split("-")[0];
				msg = msg + "-" + count;
				writer.write(msg + "-[Normal Client]");
				writer.flush();
				
				String response = null;
				while ((response = reader.readLine()) != null) {
					System.out.println(response);
				}
				
				Thread.sleep(Random.class.newInstance().nextInt(10));
			} catch (Exception exp) {
				exp.printStackTrace();
			} finally {
				try {
					writer.close();
					reader.close();
				} catch (Exception exp) {
					exp.printStackTrace();
				}
			}
		}
	}
}

class NioClient extends Thread {
	private SocketChannel socketchannel;
	private String host = "127.0.0.1";
	private int port = 11204;
	private ByteBuffer buffer = ByteBuffer.allocate(1024);
	private Charset charset = Charset.forName("UTF-8");
	private String name;
	
	public NioClient(int port) {
		this.port = port;
	}
	
	public NioClient(String name, int port) {
		this.name = name;
		this.port = port;
	}
	
	public NioClient(String name, String host, int port) {
		this(name, port);
		this.host = host;
	}
	
	@Override
	public void run() {
		int count = 20001;
		while (--count > 0) {
			try {
				socketchannel = SocketChannel.open();
				InetSocketAddress isa = new InetSocketAddress(host, port);
				socketchannel.connect(isa);
				buffer.clear();
				String input;
				if (this.name == null) {
					input = "["+ count +"]-";
				} else {
					input = "[" + this.name + "]-["+ count +"]-";
				}
				input = input + UUID.randomUUID().toString().split("-")[0];
				input = input + "-[" + count + "]";
				buffer.put(charset.encode(input));
				buffer.put(charset.encode("-[Nio Client]"));
				buffer.flip();
				socketchannel.write(buffer);
				buffer.clear();

				ByteBuffer resultBuffer = ByteBuffer.allocate(10);
				StringBuilder result = new StringBuilder();
				int readlen = 0;
				while ((readlen = socketchannel.read(resultBuffer)) > 0) {
					resultBuffer.flip();
					result.append(charset.decode(resultBuffer));
					resultBuffer.clear();
					if (readlen < 10) {
						break;
					}
				}
				System.out.println(result);
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					socketchannel.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			try {
				Thread.sleep(Random.class.newInstance().nextInt(10));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}

class NioServer extends Thread {

	private final String name;
	private ServerSocketChannel serverchannel;
	private Selector selector;
	private int port = 11204;
	private Charset charset = Charset.forName("UTF-8");
	
	public NioServer(String name, int port, Selector selector) {
		this.port = port;
		this.selector = selector;
		this.name = name;
	}

	@Override
	public void run() {
		try {
			serverchannel = ServerSocketChannel.open();
			//selector = Selector.open();
			InetSocketAddress isa = new InetSocketAddress(port);
			serverchannel.socket().bind(isa);
			serverchannel.configureBlocking(false);
			serverchannel.register(selector, SelectionKey.OP_ACCEPT);
			
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
				socketchannel.register(selector, SelectionKey.OP_READ);
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
			System.out.println("READ:" + input + "-["+ this.name +"]");
			try {
				keychannel.configureBlocking(false);
				keychannel.register(selector, SelectionKey.OP_WRITE);
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
			Thread.sleep(Random.class.newInstance().nextInt(2000));
			channel.write(charset.encode(out));
			channel.register(selector, SelectionKey.OP_READ);
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
