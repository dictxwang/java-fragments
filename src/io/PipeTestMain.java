package io;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Pipe;
import java.nio.channels.Pipe.SinkChannel;
import java.nio.channels.Pipe.SourceChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.charset.Charset;
import java.util.Iterator;

public class PipeTestMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		try {
			Pipe pipe = Pipe.open();
			
			PReader reader = new PReader(pipe);
			PWriter writer = new PWriter(pipe);

			writer.start();
			reader.start();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

class PWriter extends Thread {
	private Pipe pipe;

	public PWriter(Pipe pipe) {
		this.pipe = pipe;
	}
	@Override
	public void run() {
		try {

			Charset charset = Charset.forName("UTF-8");
			SinkChannel sinkChannel = pipe.sink();
			int count = 1;
			sinkChannel.configureBlocking(false);
			while (true) {
				ByteBuffer request = charset.encode("abc-" + count++);
				sinkChannel.write(request);
				
				sleep(100);
			}
		} catch (Exception exp) {
			exp.printStackTrace();
		}
	}
}

class PReader extends Thread {
	private Pipe pipe;
	
	public PReader(Pipe pipe) {
		this.pipe = pipe;
	}
	
	@Override
	public void run() {
		try {
			Charset charset = Charset.forName("UTF-8");
			SourceChannel sourceChannel = pipe.source();
			sourceChannel.configureBlocking(false);
			Selector selector = Selector.open();
			sourceChannel.register(selector, SelectionKey.OP_READ);
			while (true) {
				if (selector.select() < 1) {
					continue;
				}
				Iterator<SelectionKey> keys = selector.selectedKeys().iterator();
				while (keys.hasNext()) {
					SelectionKey key = keys.next();
					if (!key.isValid()) {
						continue;
					}
					keys.remove();
					
					if (key.isReadable()) {
						ByteBuffer result = ByteBuffer.allocate(64);
						sourceChannel.read(result);
						result.flip();
						String response = charset.decode(result).toString();
						System.out.println(response);
					}
				}
			}
		} catch (Exception exp) {
			exp.printStackTrace();
		}
	}
}
