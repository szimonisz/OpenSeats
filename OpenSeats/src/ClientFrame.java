import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ClientFrame extends JFrame{
	private static final int PORT = 4601;
	private JLabel _instructionsLabel;
	private final JTextField _textField;
	private String _crn;
	private String _userEmail;
	private final JTextArea _textArea;
	private String _scrapeResults;
	
	public ClientFrame() {
		super("Open Class Notifier");		
		_instructionsLabel = new JLabel("Enter a CRN");
		setLayout(new BorderLayout());
			
		_textArea = new JTextArea(10,10);
		_textField = new JTextField(40);
		
		_textField.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(_instructionsLabel.getText() == "Enter a CRN" ){
					_crn = _textField.getText();
					_instructionsLabel.setText("Enter your email!");
					_textArea.append("CRN: " + _crn + "\n");
				}else if(_instructionsLabel.getText() == "Enter your email!"){
					_userEmail = _textField.getText();
					_textArea.append("Email: " + _userEmail + "\n");
					_instructionsLabel.setText("Connecting to server...");
					//setup network after retrieving crn and userEmail
					handleNetwork();
				}
				_textField.setText("");
			}
		});
		
		add(_instructionsLabel, BorderLayout.NORTH);
		add(_textField, BorderLayout.CENTER);
		add(_textArea, BorderLayout.SOUTH);
		
		if(!(_scrapeResults == null)){
			_textArea.append(_scrapeResults);
		}
	}
	
	public void handleNetwork() {
		Socket socket = null;
		ObjectOutputStream outputStream = null;
		ObjectInputStream inputStream = null;
		
		try {
			//InetAddress address = InetAddress.getLocalHost();
			InetAddress address = InetAddress.getByName("localhost");
			socket = new Socket(address, PORT);
			
			outputStream = new ObjectOutputStream(socket.getOutputStream());
			outputStream.flush();
			
			inputStream = new ObjectInputStream(socket.getInputStream());
			
			outputStream.writeObject(_crn);
			outputStream.flush();
						
			outputStream.writeObject(_userEmail);
			outputStream.flush();			
			
			try {
				String serverMessage = (String) inputStream.readObject();
			
				_instructionsLabel.setText(serverMessage);
				
				_scrapeResults = (String) inputStream.readObject();
				
				_textArea.append(_scrapeResults);
				_instructionsLabel.setText("Thank you for using OpenSeats");
				
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
	
		} catch (IOException ex) {
			ex.printStackTrace();
		}finally {
			//Close resources in finally block so that they get closed even if we hit an exception above.
			try{
				if(socket !=null){
					socket.close();
				}
				if(outputStream != null){
					outputStream.close();
				}
				if(inputStream != null){
					inputStream.close();
				}
			} catch (IOException ex){
				ex.printStackTrace();
			}
		}
	}
}