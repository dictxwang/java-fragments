package tjms;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

public class WithActiveMq {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		MessageReceiver receiver = new MessageReceiver("R01");
		receiver.start();
		MessageReceiver receiverB = new MessageReceiver("R02");
		receiverB.start();
		
		MessageSender sender = new MessageSender();
		sender.start();
		MessageSender senderB = new MessageSender();
		senderB.start();
	}

}

class MessageSender extends Thread {
	
	@Override
	public void run() {
		
		// jms链接工厂，用它创建jms链接
		ConnectionFactory connectionFactory;
		// jms客户端到jms provider的链接
		Connection connection = null;
		// 发生或者接受消息的线程
		Session session;
		// 消息发送的目的地
		Destination destination;
		// 消息生产者
		MessageProducer producer;
		
		try {
			connectionFactory = new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_USER,
					ActiveMQConnection.DEFAULT_PASSWORD, "tcp://localhost:61616");
			// 获取connection
			connection = connectionFactory.createConnection();
			// 启动
			connection.start();
			// 获取发送或者获取消息的会话
			// 需要设置消息的签收方式
			session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);
//			session = connection.createSession(true, Session.CLIENT_ACKNOWLEDGE);
//			session = connection.createSession(true, Session.DUPS_OK_ACKNOWLEDGE);
			destination = session.createQueue("ActiveMQ_QUEUE_TEST");
			// 获取消息发送者
			producer = session.createProducer(destination);
			// 设置不可持久化
			producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
			
			int count = 0;
			while (count++ < 100000) {
				TextMessage message = session.createTextMessage("ActiveMQ发生的消息" + count);
				//System.out.println("发生消息：" + count);
				message.setJMSPriority(count % 10);
				producer.send(message);
				session.commit();
				Thread.sleep(2000);
			}
			
		} catch (Exception exp) {
			exp.printStackTrace();
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (Exception exp) {
					exp.printStackTrace();
				}
			}
		}
	}
}

class MessageReceiver extends Thread {
	
	private String name;
	
	public MessageReceiver(String name) {
		this.name = name;
	}

	@Override
	public void run() {
		
		ConnectionFactory connectionFactory;
		Connection connection = null;
		Session session;
		Destination destination;
		MessageConsumer consumer;
		try {
			connectionFactory = new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_USER,
					ActiveMQConnection.DEFAULT_PASSWORD, "tcp://localhost:61616");
			connection = connectionFactory.createConnection();
			connection.start();
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			destination = session.createQueue("ActiveMQ_QUEUE_TEST");
			consumer = session.createConsumer(destination);
			consumer.setMessageListener(new MessageListener() {
				
				@Override
				public void onMessage(Message message) {
					try {
						String result = ((TextMessage)message).getText();
						System.out.printf(">%s接受消息-%s\n", name, result);
					} catch (Exception exp) {
						exp.printStackTrace();
					}
				}
			});

		} catch (Exception exp) {
			exp.printStackTrace();
		} finally {
//			if (connection != null) {
//				try {
//					connection.close();
//				} catch (JMSException e) {
//					e.printStackTrace();
//				}
//			}
		}
	}
}