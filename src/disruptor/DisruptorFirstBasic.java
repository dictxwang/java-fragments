package disruptor;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.lmax.disruptor.BusySpinWaitStrategy;
import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.WorkHandler;
import com.lmax.disruptor.YieldingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

/**
 * 生产者消费者模型
 * @author wangqiang103
 *
 * https://www.cnblogs.com/pku-liuqiang/p/8544700.html
 * https://juejin.cn/post/6844903704777195533
 */
public class DisruptorFirstBasic {

	public static void main(String[] args) throws Exception {
//		singleProducerAndConsumer();
//		singleProducerMultiConsumerOneByOne();
//		singleProducerMultiConsumerOneTime();
//		singleProducerMultiConsumerSingleton();
//		singleProducerMultiConsumerGroupsOneByOne();
//		singleProducerMultiConsumerGroupsOneTime();
		multiProduerAndSingleConsumer();
	}
	
	// 单个生产者、消费者
	static void singleProducerAndConsumer() throws Exception {
		EventFactory<Order> factory = new OrderFactory();
		int ringBufferSize = 1024 * 1024;
		Disruptor<Order> disruptor = new Disruptor<Order>(factory, ringBufferSize,
				Executors.defaultThreadFactory(), ProducerType.SINGLE, new YieldingWaitStrategy());
		
		// set one consumer
		disruptor.handleEventsWith(new OrderHandler("singleProducerAndConsumer001"));
		disruptor.start();
		
		RingBuffer<Order> ringBuffer = disruptor.getRingBuffer();
		Producer producer = new Producer(ringBuffer);
		
		for (int i = 0; i < 3; i++) {
			producer.onData(String.valueOf(i));
		}
		
		Thread.sleep(1000);
		disruptor.shutdown();
	}
	
	// 单个生产者，多个消费者顺序消费
	static void singleProducerMultiConsumerOneByOne() throws Exception {
		EventFactory<Order> factory = new OrderFactory();
		int ringBufferSize = 1024 * 1024;
		Disruptor<Order> disruptor = new Disruptor<Order>(factory, ringBufferSize,
				Executors.defaultThreadFactory(), ProducerType.SINGLE, new YieldingWaitStrategy());
		
		// set multiple consumers
		disruptor.handleEventsWith(new OrderHandler("consumer001"))
			.then(new OrderHandler("consumer002"))
			.then(new OrderHandler("consumer003"));
		disruptor.start();
		
		RingBuffer<Order> ringBuffer = disruptor.getRingBuffer();
		Producer producer = new Producer(ringBuffer);
		
		for (int i = 0; i <= 3; i++) {
			producer.onData(String.valueOf(i));
		}
		
		TimeUnit.SECONDS.sleep(1);
		disruptor.shutdown();
	}
	
	// 单个生产者，多个消费者，一条消息仅消费一次
	static void singleProducerMultiConsumerOneTime() throws Exception {
		EventFactory<Order> factory = new OrderFactory();
		int ringBufferSize = 1024 * 1024;
		Disruptor<Order> disruptor = new Disruptor<Order>(factory, ringBufferSize,
				Executors.defaultThreadFactory(), ProducerType.SINGLE, new YieldingWaitStrategy());
		
		// set multiple consumers
		disruptor.handleEventsWithWorkerPool(new OrderHandler("consumer001"),
				new OrderHandler("consumer002"));
		disruptor.start();

		RingBuffer<Order> ringBuffer = disruptor.getRingBuffer();
		Producer producer = new Producer(ringBuffer);
		
		for (int i = 0; i <= 3; i++) {
			producer.onData(String.valueOf(i));
		}
		
		TimeUnit.SECONDS.sleep(1);
		disruptor.shutdown();
	}
	
	// 单个生产者，多个消费者独立消费
	static void singleProducerMultiConsumerSingleton() throws Exception {
		EventFactory<Order> factory = new OrderFactory();
		int ringBufferSize = 1024 * 1024;
		Disruptor<Order> disruptor = new Disruptor<Order>(factory, ringBufferSize,
				Executors.defaultThreadFactory(), ProducerType.SINGLE, new YieldingWaitStrategy());

		// set multiple consumers
		disruptor.handleEventsWith(new OrderHandler("consumer001"),
				new OrderHandler("consumer002"));
		disruptor.start();
		
		RingBuffer<Order> ringBuffer = disruptor.getRingBuffer();
		Producer producer = new Producer(ringBuffer);
		
		for (int i = 0; i <= 3; i++) {
			producer.onData(String.valueOf(i));
		}
		
		TimeUnit.SECONDS.sleep(1);
		disruptor.shutdown();
	}
	
	// 单个生产者，多个消费者分组消费，每条消息均被组内所有消费者消费
	static void singleProducerMultiConsumerGroupsOneByOne() throws Exception {
		EventFactory<Order> factory = new OrderFactory();
		int ringBufferSize = 1024 * 1024;
		Disruptor<Order> disruptor = new Disruptor<Order>(factory, ringBufferSize,
				Executors.defaultThreadFactory(), ProducerType.SINGLE, new YieldingWaitStrategy());
		
		// set multiple groups
		disruptor.handleEventsWith(new OrderHandler("consumer001"), new OrderHandler("consumer002"))
			.then(new OrderHandler("consumer003"))
			.then(new OrderHandler("consumer004"), new OrderHandler("consumer005"));
		disruptor.start();
		
		RingBuffer<Order> ringBuffer = disruptor.getRingBuffer();
		Producer producer = new Producer(ringBuffer);
		
		for (int i = 0; i <= 3; i++) {
			producer.onData(String.valueOf(i));
		}
		
		TimeUnit.SECONDS.sleep(1);
		disruptor.shutdown();
	}
	
	// 单个生产者，多个消费者分组消费，每条消息在同一个组内不重复消费
	static void singleProducerMultiConsumerGroupsOneTime() throws Exception {
		EventFactory<Order> factory = new OrderFactory();
		int ringBufferSize = 1024 * 1024;
		Disruptor<Order> disruptor = new Disruptor<Order>(factory, ringBufferSize,
				Executors.defaultThreadFactory(), ProducerType.SINGLE, new YieldingWaitStrategy());
		
		// set multiple groups
		disruptor.handleEventsWithWorkerPool(new OrderHandler("consumer001"), new OrderHandler("consumer002"))
			.handleEventsWithWorkerPool(new OrderHandler("consumer003"))
			.handleEventsWithWorkerPool(new OrderHandler("consumer004"), new OrderHandler("consumer005"));
		disruptor.start();
		
		RingBuffer<Order> ringBuffer = disruptor.getRingBuffer();
		Producer producer = new Producer(ringBuffer);
		
		for (int i = 0; i <= 3; i++) {
			producer.onData(String.valueOf(i));
		}
		
		TimeUnit.SECONDS.sleep(1);
		disruptor.shutdown();
	}
	
	// 多个生产者，单个消费者
	static void multiProduerAndSingleConsumer() throws Exception {
		EventFactory<Order> factory = new OrderFactory();
		int ringBufferSize = 1024;
		Disruptor<Order> disruptor = new Disruptor<Order>(factory, ringBufferSize,
				Executors.defaultThreadFactory(), ProducerType.MULTI, new BusySpinWaitStrategy());
		
		// set single consumer
		disruptor.handleEventsWith(new OrderHandler("consumer001"));
		disruptor.start();

        final RingBuffer<Order> ringBuffer = disruptor.getRingBuffer();
		
		// start multiple producers
		for (int i = 1; i <= 50; i++) {
			final int index = i;
			final Producer producer = new Producer(ringBuffer);
			Thread t = new Thread() {
				@Override
				public void run() {
					for (int j = 0; j < 10; j++) {
						producer.onData("producer-" + index + ",message-" + j);
					}
				}
			};
			t.start();
		}
		
		TimeUnit.SECONDS.sleep(5);
		disruptor.shutdown();
	}
	
	static class Order {
		private String id;
		
		public String getId() {
	        return id;
	    }
	    public void setId(String id) {
	        this.id = id;
	    }
	}

	static class OrderFactory implements EventFactory<Order> {
		
		public Order newInstance() {
			return new Order();
		}
	}
	
	// EventHandler用于EventHandlerGroup，WorkHandler用于WorkPool。
	// 同时实现两接口，该类对象可同时用于EventHandlerGroup和WorkPool
	static class OrderHandler implements EventHandler<Order>, WorkHandler<Order> {
		private String consumerId;
		
		public OrderHandler(String consumerId) {
			this.consumerId = consumerId;
		}

		@Override
		public void onEvent(Order event) throws Exception {
			System.out.println("OrderWorkHandler-" + this.consumerId + " consume message: " + event.getId());
		}

		@Override
		public void onEvent(Order event, long sequence, boolean endOfBatch) throws Exception {
			System.out.println("OrderEventHandler-" + this.consumerId + " consume message: " + event.getId() + ",sequence: " + sequence);
		}
	}
	
	static class Producer {
		
		private final RingBuffer<Order> ringBuffer;
		
		public Producer(RingBuffer<Order> ringBuffer) {
			this.ringBuffer = ringBuffer;
		}
		
		public void onData(String data) {
			long sequence = this.ringBuffer.next();
			try {
				Order order = this.ringBuffer.get(sequence);
				order.setId(data);
			} catch (Exception exp) {
				exp.printStackTrace();
			} finally {
				this.ringBuffer.publish(sequence);
			}
		}
	}
}
