package collection;

import java.util.UUID;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;

public class WithSynchronousQueue {

	public static void main(String[] args) {
		
		SynchronousQueue<String> queue = new SynchronousQueue<String>();
		ReadThread readThreadOne = new ReadThread("R001", queue);
		ReadThread readThreadTwo = new ReadThread("R002", queue);
		
		WriteThread writeThreadOne = new WriteThread("W001", queue);
		WriteThread writeThreadTwo = new WriteThread("W002", queue);
		WriteThread writeThreadThree = new WriteThread("W003", queue);
		WriteThread writeThreadFour = new WriteThread("W004", queue);
		
		readThreadOne.start();
		readThreadTwo.start();
		
		writeThreadOne.start();
		writeThreadTwo.start();
		writeThreadThree.start();
		writeThreadFour.start();
	}
}

class WriteThread extends Thread {
	String name = "";
	SynchronousQueue<String> queue = null;
	
	public WriteThread(String name, SynchronousQueue<String> queue) {
		this.name = name;
		this.queue = queue;
	}
	
	@Override
	public void run() {
		while (true) {
			String value = UUID.randomUUID().toString();
			try {
				this.queue.offer(value, 500, TimeUnit.MILLISECONDS);
				System.out.printf("[%s] put a element - %s\n", name, value);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}

class ReadThread extends Thread {
	
	String name = "";
	SynchronousQueue<String> queue = null;
	
	public ReadThread(String name, SynchronousQueue<String> queue) {
		this.name = name;
		this.queue = queue;
	}
	@Override
	public void run() {
		String value = null;
		while (true) {
			try {
				value = queue.poll(1, TimeUnit.SECONDS);
				System.out.printf("[%s] take a element - %s\n", this.name, value);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}