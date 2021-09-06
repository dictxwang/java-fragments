package tjms;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

public class WithActiveMqReply {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		MessageWithReplySender sender = new MessageWithReplySender("S01");
		MessageWithReplyConsumer consumer = new MessageWithReplyConsumer("c01");
		
		consumer.start();
		sender.start();
	}
}

class MessageWithReplySender extends Thread {
	private String name;
	public MessageWithReplySender(String name) {
		this.name = name;
	}
	
	@Override
	public void run() {
		ConnectionFactory connectionFactory;
		Connection connection = null;
		try {
			connectionFactory = new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_USER,
					ActiveMQConnection.DEFAULT_PASSWORD, "tcp://localhost:61616");
			connection = connectionFactory.createConnection();
			connection.start();
			Session session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);
			Destination destination = session.createQueue("Test_reply_Queue");
			MessageProducer producer = session.createProducer(destination);
			producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
			
			Destination destinationReply = session.createQueue("Test_reply_answer_Queue");
			MessageConsumer consumer = session.createConsumer(destinationReply);
			consumer.setMessageListener(new MessageListener() {
				
				@Override
				public void onMessage(Message message) {
					try {
						String content = ((TextMessage)message).getText();
						System.out.printf("[发送端]接收到应答-%s\n", content);
					} catch (JMSException e) {
						e.printStackTrace();
					}
				}
			});
			int count = 0;
			while (count++ < 10) {
				Message message = session.createTextMessage(this.name + "发送的消息," + count);
				message.setJMSReplyTo(destinationReply);
				producer.send(message);
				session.commit();
				
				Thread.sleep(500);
			}
		} catch (Exception exp) {
			exp.printStackTrace();
		} finally {
//			try {
//				connection.close();
//			} catch (JMSException e) {
//				e.printStackTrace();
//			}
		}
	}
}

class MessageWithReplyConsumer extends Thread{
	private String name;
	public MessageWithReplyConsumer(String name) {
		this.name = name;
	}
	
	@Override
	public void run() {
		ConnectionFactory connectionFactory;
		Connection connection = null;
		try {
			connectionFactory = new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_USER,
					ActiveMQConnection.DEFAULT_PASSWORD, "tcp://localhost:61616");
			connection = connectionFactory.createConnection();
			connection.start();
			final Session session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);
			Destination destination = session.createQueue("Test_reply_Queue");
			MessageConsumer consumer = session.createConsumer(destination);
			
			consumer.setMessageListener(new MessageListener() {
				
				@Override
				public void onMessage(Message message) {
					try {
						ExecuteThread executor = new ExecuteThread(name, session, message);
						executor.start();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		} catch (Exception exp) {
			exp.printStackTrace();
		} finally {
			// 接受到消息前，不能关闭connection
//			try {
//				connection.close();
//			} catch (JMSException e) {
//				e.printStackTrace();
//			}
		}
	}
}

class ExecuteThread extends Thread {
	
	String name;
	Session session;
	Message message;
	public ExecuteThread(String name, Session session, Message message) {
		this.name = name;
		this.session = session;
		this.message = message;
	}
	
	@Override
	public void run() {
		try {
			System.out.printf("[消费端]接收到请求-%s\n", ((TextMessage)message).getText());
			String count = ((TextMessage)message).getText().split(",")[1];
			
			Destination destination = message.getJMSReplyTo();
			MessageProducer producer = session.createProducer(destination);
			Message messageReply = session.createTextMessage(this.name + "回复的消息," + count);
			producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
			producer.send(messageReply);
			Thread.sleep(3000);
			session.commit();
		} catch (Exception exp) {
			exp.printStackTrace();
		}
	}
}