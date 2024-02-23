package concurrent;

import java.util.concurrent.locks.AbstractQueuedSynchronizer;

public class MyLockBasic {

	static int count = 0;
	static MyLock lock = new MyLock();
	
	public static void main(String[] args) throws InterruptedException {
		

		Runnable runnable = new Runnable() {
			
			@Override
			public void run() {
				
				for (int i = 0; i < 10000; i++) {
					try {
						lock.lock();
						count++;
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						lock.release();
					}
				}
			}
		};
		
		Thread t1 = new Thread(runnable);
		Thread t2 = new Thread(runnable);
		
		t1.start();
		t2.start();
		
		t1.join();
		t2.join();
		
		System.out.println(count);  // 20000
	}

	static class MyLock {
		
		private static class Sync extends AbstractQueuedSynchronizer {
			
			@Override
			protected boolean tryAcquire(int arg) {
				return compareAndSetState(0, 1);
			}
			
			@Override
			protected boolean tryRelease(int arg) {
				setState(0);
				return true;
			}
			
			@Override
			protected boolean isHeldExclusively() {
				return getState() == 1;
			}
		}
		
		private Sync sync = new Sync();
		
		public void lock() {
			sync.acquire(1);
		}
		
		public void release() {
			sync.release(1);
		}
		
		public void unlock() {}
	}
}
