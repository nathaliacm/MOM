package Broker;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.BrokerService;
import org.apache.activemq.command.ActiveMQDestination;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.command.ActiveMQTopic;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Session;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.jms.*;

public class BrokerModel {
	
	private BrokerService broker = new BrokerService();
	ConnectionFactory connectionFactory;
	Connection connection;
	Session session;
    
    public void start() {
        try {
            String url = ActiveMQConnection.DEFAULT_BROKER_URL;
    		connectionFactory = new ActiveMQConnectionFactory(url);
    		connection = connectionFactory.createConnection();
    		connection.start();
    		session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
    
    public void stop() {
        try {
            session.close();
            connection.close();
            broker.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    // Queues
    
    public void createQueue(String queueName) throws JMSException {
    	Destination destination = session.createQueue(queueName);      
	    MessageProducer producer = session.createProducer(destination);
	    producer.close();
    }
    
    public Map<String, Integer> getQueues() throws JMSException {
        Map<String, Integer> queues = new TreeMap<>();
        Set<ActiveMQQueue> queuesMQ = ((ActiveMQConnection) connection).getDestinationSource().getQueues();
        
        queuesMQ.forEach(queue -> {
			List<String> messages;
			try {
				messages = getMessages(queue.getPhysicalName());
				queues.put(queue.getPhysicalName(), messages.size());
			} catch (JMSException e) {
				e.printStackTrace();
			}
			

		});
        
        return queues;
    }

    public List<String> getMessages(String queueName) throws JMSException {
        Queue queue = session.createQueue(queueName);
        QueueBrowser browser = session.createBrowser(queue);
        List<String> messages = new ArrayList<>();

        try {
            Enumeration<?> enumeration = browser.getEnumeration();

            while (enumeration.hasMoreElements()) {
                TextMessage message = (TextMessage) enumeration.nextElement();
                messages.add(message.getText());
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (browser != null) {
                browser.close();
            }
        }

        return messages;
    }

    
    public void deleteQueue(String queueName) throws JMSException {
        Destination destination = session.createQueue(queueName);
        ((ActiveMQConnection) connection).destroyDestination((ActiveMQDestination) destination);
    }
    
    // Topics
    
    public void createTopic(String name) throws JMSException {
	    Destination destination = session.createTopic(name);      
		MessageProducer publisher = session.createProducer(destination);
		publisher.close();
		
	}
	
	public void deleteTopic(String name) throws JMSException {
	      Destination destination = session.createTopic(name);      
	      ((ActiveMQConnection) connection).destroyDestination((ActiveMQDestination) destination);
	}
	
	public List<String> getTopics() throws JMSException {
		Set<ActiveMQTopic> topics = ((ActiveMQConnection) connection).getDestinationSource().getTopics();
		List<String> topicsNames = new ArrayList<>();
		topics.forEach(fila -> {
			String nome = fila.getPhysicalName();
			topicsNames.add(nome);

		});
		return topicsNames;
	}
}
