package Client;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

import javax.jms.*;

public class ClientModel {
	
	ConnectionFactory connectionFactory;
	private Connection connection;
    private Session queueSession;
    private Session topicSession;
    
    private Map<String, MessageConsumer> topicConsumers = new HashMap<>();
    private Map<String, MessageListener> topicListeners = new HashMap<>();
    
    public void start() {
        try {
            String url = ActiveMQConnection.DEFAULT_BROKER_URL;
    		connectionFactory = new ActiveMQConnectionFactory(url);
    		connection = connectionFactory.createConnection();
    		connection.start();
    		
    		queueSession = connection.createSession(Session.AUTO_ACKNOWLEDGE);
            topicSession = connection.createSession(Session.AUTO_ACKNOWLEDGE);

        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
    
    public void stop() {
        try {
            queueSession.close();
            topicSession.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    // Queues
    
    public void sendMessageToQueue(String queueName, String message) throws JMSException {    	
        Destination destination = queueSession.createQueue(queueName);
        MessageProducer producer = queueSession.createProducer(destination);
        producer.send(queueSession.createTextMessage(message));
        producer.close();
    }
    
    public List<String> receiveMessagesFromQueue(String queueName) throws JMSException {    	
        Destination destination = queueSession.createQueue(queueName);
        MessageConsumer consumer = queueSession.createConsumer(destination);
        List<String> messages = new ArrayList<>();
        TextMessage message;

        while ((message = (TextMessage) consumer.receive(10)) != null) {
            messages.add(message.getText());
        }

        consumer.close();

        return messages;
    }
    
    // Topics
    
    public void sendMessageToTopic(String topicName, String message) throws JMSException {
        Destination destination = topicSession.createTopic(topicName);
        MessageProducer producer = topicSession.createProducer(destination);
        producer.send(topicSession.createTextMessage(message));
        producer.close();
    }

    public void subscribeToTopic(String topic, BiConsumer<String, String> listener) throws JMSException {
        Destination destination = topicSession.createTopic(topic);
        MessageConsumer consumer = topicSession.createConsumer(destination);

        topicConsumers.put(topic, consumer);

        MessageListener onMessage = createMessageListener(listener, topic);
        
        topicListeners.put(topic, onMessage);
        consumer.setMessageListener(onMessage);
    }

    private MessageListener createMessageListener(BiConsumer<String, String> listener, String topicName) {
        return message -> {
            if (message instanceof TextMessage) {
                try {
                    String lastMessage = ((TextMessage) message).getText();
                    listener.accept(lastMessage, topicName);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }
}
