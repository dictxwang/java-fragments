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

public class WithActiveMqTopic {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		TopicConsumer consumerA = new TopicConsumer("C01");
		TopicConsumer consumerB = new TopicConsumer("C02");
		consumerA.start();
		consumerB.start();
		
		TopicProducer producer = new TopicProducer();
		producer.start();
	}

}

class TopicProducer extends Thread {
	private String name;
	
	public TopicProducer() {
	}
	public TopicProducer(String name) {
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
			Destination destination = session.createTopic("ActiveMQ_Topic_test");
			MessageProducer producer = session.createProducer(destination);
			producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
			int count = 0;
			while (count++ < 1000) {
				TextMessage message = session.createTextMessage("ActiveMQ的订阅消息" + count);
				producer.send(message);
				session.commit();
				Thread.sleep(2000);
			}
		} catch (Exception exp) {
			exp.printStackTrace();
		} finally {
			try {
				connection.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}

class TopicConsumer extends Thread {
	private String name;
	
	public TopicConsumer(String name) {
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
			Destination destination = session.createTopic("ActiveMQ_Topic_test");
			MessageConsumer consumer = session.createConsumer(destination);
			consumer.setMessageListener(new MessageListener() {
				
				@Override
				public void onMessage(Message message) {
					try {
						System.out.printf("%s接受订阅消息-%s\n", name, ((TextMessage)message).getText());
						message.getJMSReplyTo();
					} catch (JMSException e) {
						e.printStackTrace();
					}
				}
			});
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