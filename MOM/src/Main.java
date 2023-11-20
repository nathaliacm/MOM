import javax.jms.JMSException;

import Broker.BrokerModel;
import Broker.BrokerView;

public class Main {
	public static void main(String[] args) throws JMSException {	
		BrokerView view = new BrokerView();
		view.start();	
	}
}