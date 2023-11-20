package Broker;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.jms.JMSException;
import javax.swing.JTextField;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import javax.swing.JLabel;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import Client.ClientView;

public class BrokerView {
	
	private BrokerModel broker;

	private JFrame frame;
	private JTextField textField;
	private JTextField textField_1;
	DefaultTableModel queues = new DefaultTableModel();
	DefaultTableModel topics = new DefaultTableModel();

	/**
	 * Launch the application.
	 */
	public void start() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					BrokerView window = new BrokerView();
					window.frame.setVisible(true);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public BrokerView() {
		broker = new BrokerModel();
		broker.start();
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		queues.setColumnIdentifiers(new String[]{"Fila", "Mensagens"});
		topics.setColumnIdentifiers(new String[]{"Tópicos"});
		updateQueues();
		updateTopics();
		
		JTable queueTable = new JTable(queues);
		queueTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		queueTable.setFillsViewportHeight(true);
		
		JTable topicsTable = new JTable(topics);
		topicsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		topicsTable.setFillsViewportHeight(true);
		
		frame = new JFrame();
		frame.setBounds(100, 100, 936, 623);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JPanel panel = new JPanel();
		frame.getContentPane().add(panel);
		panel.setBounds(0, 0, 468, 520);
		panel.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Filas");
		lblNewLabel.setBounds(195, 5, 58, 30);
		lblNewLabel.setFont(new Font("Lucida Grande", Font.BOLD, 24));
		panel.add(lblNewLabel);
		
		JLabel lblNewLabel_2 = new JLabel("Nome da fila");
		lblNewLabel_2.setFont(new Font("Lucida Grande", Font.PLAIN, 14));
		lblNewLabel_2.setBounds(16, 60, 93, 16);
		panel.add(lblNewLabel_2);
		
		textField = new JTextField();
		textField.setBounds(110, 56, 245, 26);
		panel.add(textField);
		textField.setColumns(10);
		
		JButton btnNewButton_1 = new JButton("Adicionar");
		btnNewButton_1.setBounds(357, 56, 105, 29);
		panel.add(btnNewButton_1);
		
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String queueName = textField.getText(); 
				if (!queueName.isEmpty()) {
					try {
						broker.createQueue(queueName);
						updateQueues();
						textField.setText("");
					} catch (JMSException e1) {
						e1.printStackTrace();
					}
				}
			}
		});
		
		JButton btnNewButton_3 = new JButton("Remover");
		btnNewButton_3.setBounds(345, 407, 117, 29);
		panel.add(btnNewButton_3);
		btnNewButton_3.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				try {
					int selectedQueue = queueTable.getSelectedRow();
					System.out.println(selectedQueue);
					if (selectedQueue < 0) {
						return;
					}
					String queueName = (String) queues.getValueAt(selectedQueue, 0);
					broker.deleteQueue(queueName);
					updateQueues();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		JPanel panel_1 = new JPanel();
		frame.getContentPane().add(panel_1);
		panel_1.setBounds(468, 0, 468, 520);
		panel_1.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(16, 124, 433, 252);
		panel.add(scrollPane);

		scrollPane.setViewportView(queueTable);
		
		JButton btnNewButton_5 = new JButton("Atualizar");
		btnNewButton_5.setBounds(238, 407, 117, 29);
		btnNewButton_5.addActionListener(new ActionListener () {
			@Override
			public void actionPerformed(ActionEvent event) {
				updateQueues();
			}
		});
		panel.add(btnNewButton_5);
		
		JLabel lblNewLabel_1 = new JLabel("Tópicos");
		lblNewLabel_1.setBounds(211, 5, 98, 30);
		lblNewLabel_1.setFont(new Font("Lucida Grande", Font.BOLD, 24));
		panel_1.add(lblNewLabel_1);
		
		JLabel lblNewLabel_3 = new JLabel("Nome do tópico");
		lblNewLabel_3.setFont(new Font("Lucida Grande", Font.PLAIN, 14));
		lblNewLabel_3.setBounds(16, 60, 110, 16);
		panel_1.add(lblNewLabel_3);
		
		textField_1 = new JTextField();
		textField_1.setBounds(130, 56, 232, 26);
		panel_1.add(textField_1);
		textField_1.setColumns(10);
		
		JButton btnNewButton_2 = new JButton("Adicionar");
		btnNewButton_2.setBounds(357, 56, 105, 29);
		btnNewButton_2.addActionListener(new ActionListener () {
			@Override
			public void actionPerformed(ActionEvent event) {
				try {
					String topicName = textField_1.getText();
					if (topicName == null || topicName.equals("")) {
						return;
					}
					textField_1.setText("");
					broker.createTopic(topicName);
					updateTopics();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		panel_1.add(btnNewButton_2);
		
		JButton btnNewButton_4 = new JButton("Remover");
		btnNewButton_4.setBounds(345, 407, 117, 29);
		btnNewButton_4.addActionListener(new ActionListener () {
			@Override
			public void actionPerformed(ActionEvent event) {
				try {
					int selectedRow = topicsTable.getSelectedRow();
					Object selectedValue = topicsTable.getValueAt(selectedRow, 0);
				    String topicName = selectedValue.toString();

					broker.deleteTopic(topicName);
					updateTopics();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		panel_1.add(btnNewButton_4);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(16, 124, 433, 252);
		panel_1.add(scrollPane_1);
		scrollPane_1.setViewportView(topicsTable);
		
		JButton btnNewButton_6 = new JButton("Atualizar");
		btnNewButton_6.setBounds(235, 407, 117, 29);
		btnNewButton_6.addActionListener(new ActionListener () {
			@Override
			public void actionPerformed(ActionEvent event) {
				updateTopics();
			}
		});
		panel_1.add(btnNewButton_6);
		
		JButton btnNewButton = new JButton("Instanciar cliente");
		btnNewButton.setBounds(392, 543, 157, 35);
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ClientView clientView = new ClientView();
				clientView.start();
			}
		});
		frame.getContentPane().add(btnNewButton);
		
		
	}
	
	public void updateQueues() {
		try {
			Map<String, Integer> queueInfo = broker.getQueues();
			queues.setRowCount(0);
			for (String queue : queueInfo.keySet()) {
				queues.addRow(new Object[] {queue, queueInfo.get(queue)});
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void updateTopics() {
		try {
			topics.setRowCount(0);
			for (String topic : broker.getTopics()) {
				topics.addRow(new Object[] {topic});
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
