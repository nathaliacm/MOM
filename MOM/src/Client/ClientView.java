package Client;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JLabel;

import java.awt.Font;
import javax.swing.JTextField;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.util.List;
import java.awt.event.ActionEvent;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class ClientView {
	
	private ClientModel client;

	private JFrame frame;
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	private JTextField textField_3;
	private JTextField textField_4;
	private JTextField textField_5;

	/**
	 * Launch the application.
	 */
	public void start() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ClientView window = new ClientView();
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
	public ClientView() {
		client = new ClientModel();
		client.start();
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 936, 623);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.X_AXIS));
		
		JPanel panel = new JPanel();
		frame.getContentPane().add(panel);
		panel.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Filas");
		lblNewLabel.setBounds(195, 5, 58, 30);
		lblNewLabel.setFont(new Font("Lucida Grande", Font.BOLD, 24));
		panel.add(lblNewLabel);
		
		JLabel lblNewLabel_2 = new JLabel("Nome da fila");
		lblNewLabel_2.setFont(new Font("Lucida Grande", Font.PLAIN, 14));
		lblNewLabel_2.setBounds(20, 71, 86, 16);
		panel.add(lblNewLabel_2);
		
		textField = new JTextField();
		textField.setBounds(113, 67, 296, 26);
		panel.add(textField);
		textField.setColumns(10);
		
		JTextArea textArea = new JTextArea();
		
		JButton btnNewButton = new JButton("Receber mensagens");
		btnNewButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					String queueName = textField.getText();
					if (queueName == null || queueName.equals("")) {
						return;
					}
					List<String> messages = client.receiveMessagesFromQueue(queueName);
					for (String message : messages) {
						textArea.append(message + '\n');
					}
				} catch (Exception err) {
					err.printStackTrace();
				}
			}
		});
		btnNewButton.setBounds(151, 105, 150, 35);
		panel.add(btnNewButton);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(20, 162, 389, 265);
		panel.add(scrollPane);
		
		scrollPane.setViewportView(textArea);
		
		JLabel lblNewLabel_3 = new JLabel("Mensagens:");
		lblNewLabel_3.setBounds(20, 138, 75, 16);
		panel.add(lblNewLabel_3);
		
		JLabel lblNewLabel_4 = new JLabel("Destinatário");
		lblNewLabel_4.setBounds(20, 477, 86, 16);
		panel.add(lblNewLabel_4);
		
		JLabel lblNewLabel_5 = new JLabel("Mensagem");
		lblNewLabel_5.setBounds(20, 518, 86, 16);
		panel.add(lblNewLabel_5);
		
		textField_1 = new JTextField();
		textField_1.setBounds(109, 472, 300, 26);
		panel.add(textField_1);
		textField_1.setColumns(10);
		
		textField_2 = new JTextField();
		textField_2.setBounds(109, 513, 300, 26);
		panel.add(textField_2);
		textField_2.setColumns(10);
		
		JButton btnNewButton_1 = new JButton("Enviar");
		btnNewButton_1.setBounds(292, 551, 117, 29);
		btnNewButton_1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					String destination = textField_1.getText();
					String message = textField_2.getText();

					client.sendMessageToQueue(destination, message);
					textField_1.setText("");
					textField_2.setText("");
				} catch (Exception err) {
					err.printStackTrace();
				}
			}
		});
		panel.add(btnNewButton_1);
		
		JPanel panel_1 = new JPanel();
		frame.getContentPane().add(panel_1);
		panel_1.setLayout(null);
		
		JLabel lblNewLabel_1 = new JLabel("Tópicos");
		lblNewLabel_1.setBounds(211, 5, 98, 30);
		lblNewLabel_1.setFont(new Font("Lucida Grande", Font.BOLD, 24));
		panel_1.add(lblNewLabel_1);
		
		JLabel lblNewLabel_6 = new JLabel("Nome do tópico");
		lblNewLabel_6.setFont(new Font("Lucida Grande", Font.PLAIN, 14));
		lblNewLabel_6.setBounds(20, 70, 115, 16);
		panel_1.add(lblNewLabel_6);
		
		textField_3 = new JTextField();
		textField_3.setBounds(143, 66, 291, 26);
		panel_1.add(textField_3);
		textField_3.setColumns(10);
		
		JTextArea textArea_1 = new JTextArea();
		
		JButton btnNewButton_2 = new JButton("Assinar");
		btnNewButton_2.setBounds(203, 103, 117, 35);
		btnNewButton_2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					String topicName = textField_3.getText();
					client.subscribeToTopic(topicName, (String message, String source) -> {
						textArea_1.append("(" + source + ") " + message + '\n');
					});
					textField_3.setText("");
				} catch (Exception err) {
					err.printStackTrace();
				}
			}
		});
		panel_1.add(btnNewButton_2);
		
		JLabel lblNewLabel_7 = new JLabel("Mensagens:");
		lblNewLabel_7.setFont(new Font("Lucida Grande", Font.PLAIN, 13));
		lblNewLabel_7.setBounds(20, 138, 75, 16);
		panel_1.add(lblNewLabel_7);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(20, 162, 420, 265);
		panel_1.add(scrollPane_1);
		
		scrollPane_1.setViewportView(textArea_1);
		
		JLabel lblNewLabel_8 = new JLabel("Publicação:");
		lblNewLabel_8.setBounds(20, 439, 75, 16);
		panel_1.add(lblNewLabel_8);
		
		JLabel lblNewLabel_9 = new JLabel("Tópico");
		lblNewLabel_9.setBounds(20, 477, 61, 16);
		panel_1.add(lblNewLabel_9);
		
		JLabel lblNewLabel_10 = new JLabel("Mensagem");
		lblNewLabel_10.setBounds(20, 518, 75, 16);
		panel_1.add(lblNewLabel_10);
		
		textField_4 = new JTextField();
		textField_4.setBounds(115, 513, 325, 26);
		panel_1.add(textField_4);
		textField_4.setColumns(10);
		
		textField_5 = new JTextField();
		textField_5.setBounds(114, 472, 325, 26);
		panel_1.add(textField_5);
		textField_5.setColumns(10);
		
		JButton btnNewButton_3 = new JButton("Enviar");
		btnNewButton_3.setBounds(323, 551, 117, 29);
		btnNewButton_3.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					String destination = textField_5.getText();
					String message = textField_4.getText();
					
					client.sendMessageToTopic(destination, message);
					textField_4.setText("");
				} catch (Exception err) {
					err.printStackTrace();
				}
			}
		});
		panel_1.add(btnNewButton_3);
	}
}
