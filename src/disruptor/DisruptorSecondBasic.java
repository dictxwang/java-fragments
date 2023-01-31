package disruptor;

import java.nio.ByteBuffer;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.YieldingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

/**
 * Disruptor的DSL风格实现
 * DSL：domain-specific language 领域特定语言
 * @author wangqiang103
 *
 * http://ifeve.com/disruptor-getting-started/
 */
public class DisruptorSecondBasic {

	public static void main(String[] args) throws Exception {
		// Executor that will be used to construct new threads for consumers
		ThreadFactory threadFactory = Executors.defaultThreadFactory();
		// The factory for the event
		LongEventFactory factory = new LongEventFactory();
		// Specify the size of the ring buffer, must be power of 2
		int bufferSize = 1024;
		// Construct the Disruptor
		Disruptor<LongEvent> disruptor = new Disruptor<LongEvent>(factory, bufferSize, threadFactory,
				ProducerType.SINGLE, new YieldingWaitStrategy());
		// Connect the handler
		disruptor.handleEventsWith(new LongEventHandler());
		// Start the Disruptor, starts all threads running
		disruptor.start();
		
		// Get the ring buffer from the Disruptor to be use for publishing
		RingBuffer<LongEvent> ringBuffer = disruptor.getRingBuffer();
		LongEventProducer producer = new LongEventProducer(ringBuffer);
		
		ByteBuffer bb = ByteBuffer.allocate(8);
		for (long l = 0; l < 100; l++) {
			bb.putLong(0, l);
			producer.onData(bb);
			Thread.sleep(1000);
		}
	}
	
	static class LongEvent {
		private long event;

		public long getEvent() {
			return event;
		}

		public void setEvent(long event) {
			this.event = event;
		}
	}
	
	static class LongEventFactory implements EventFactory<LongEvent> {

		@Override
		public LongEvent newInstance() {
			return new LongEvent();
		}
	}
	
	static class LongEventHandler implements EventHandler<LongEvent> {

		@Override
		public void onEvent(LongEvent event, long sequence, boolean endOfBatch) throws Exception {
			System.out.println(event.getEvent());
		}
	}
	
	static class LongEventProducer {
		private final RingBuffer<LongEvent> ringBuffer;

		public LongEventProducer(RingBuffer<LongEvent> ringBuffer) {
			super();
			this.ringBuffer = ringBuffer;
		}
		
		public void onData(ByteBuffer bb) {
			long sequence = this.ringBuffer.next();
			try {
				LongEvent event = this.ringBuffer.get(sequence);
				event.setEvent(bb.getLong(0));
			} catch (Exception exp) {
				exp.printStackTrace();
			} finally {
				this.ringBuffer.publish(sequence);
			}
		}
	}
}
