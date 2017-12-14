import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {
	public static final int PORT = 1337;
	public static final int password = 69;
	
	public static void main(String [] args){
		
		Socket socket = null;
		ObjectOutputStream outputStream = null;
		ObjectInputStream inputStream = null;
			
		try {
			InetAddress address = InetAddress.getLocalHost();
			socket = new Socket(address, PORT);
			
			outputStream = new ObjectOutputStream(socket.getOutputStream());
			outputStream.flush();
			
			inputStream = new ObjectInputStream(socket.getInputStream());
			
			Scanner scan =  new Scanner(System.in);
			
			String crn = "";
			String userEmail = "";
			
			Boolean continueScanning = true;
			while(continueScanning){
				
				System.out.println("Enter a CRN");
				crn = scan.nextLine();
				
				
				if(!crn.isEmpty()){
					continueScanning = false;
				}
				
				else{
					try{
						int inputtedInt = Integer.parseInt(crn);
						
					} catch(NumberFormatException e){
						System.out.println("Invalid CRN. Must be an integer value.");
					}		
				}
				System.out.println("Enter your email address");
				userEmail = scan.nextLine();
				
				if(!userEmail.isEmpty()){
					continueScanning = false;
				}
				
				
			}
			scan.close();
			
			outputStream.writeObject(crn);
			outputStream.flush();
						
			outputStream.writeObject(userEmail);
			outputStream.flush();			
			
			try {
				String serverMessage = (String) inputStream.readObject();
			
				System.out.println("client recieved: " + serverMessage);
				
				String scrapeResults = (String) inputStream.readObject();
				
				System.out.println(scrapeResults);
				
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