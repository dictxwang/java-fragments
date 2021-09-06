package io;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.UUID;

public class PipeStreamTestMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		PipedOutputStream postream = new PipedOutputStream();
		PipedInputStream pistream = new PipedInputStream();
		try {
			//postream.connect(pistream);
			pistream.connect(postream);
			PIStream reader = new PIStream(pistream);
			POStream writer = new POStream(postream);
			
			reader.start();
			writer.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

class PIStream extends Thread {
	private PipedInputStream pistream;
	
	public PIStream(PipedInputStream pistream) {
		this.pistream = pistream;
	}
	
	@Override
	public void run() {
		int count = 1;
		while (count++ <= 10) {
			try {
				byte[] buffer = new byte[64];
				int readlength = pistream.read(buffer);
				String response = new String(buffer, 0, readlength, "UTF-8");
				System.out.println("[Response]-" + response);
				sleep(100);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}
}

class POStream extends Thread {
	private PipedOutputStream postream;

	public POStream(PipedOutputStream postream) {
		this.postream = postream;
	}
	@Override
	public void run() {
		int count = 1;
		while (count++ <= 10) {
			try {
				String msg = UUID.randomUUID().toString();
				postream.write(msg.getBytes("UTF-8"));
				postream.flush();
				
				sleep(100);
			} catch (Exception exp) {
				exp.printStackTrace();
			}
		}
	}
}