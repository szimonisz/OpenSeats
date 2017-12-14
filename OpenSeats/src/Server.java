import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	private static final int PORT = 4601;
	
	public static void main(String [] args){
		System.out.println("running server!");
		
		ServerSocket serverSocket = null;
		Socket clientSocket = null;
		boolean listeningSocket = true;
		
		try {
			serverSocket = new ServerSocket(PORT);
			System.out.println("server socket created");
			
			while(listeningSocket){
				clientSocket = serverSocket.accept();
				System.out.println("got socket: " + clientSocket);
				MiniServer mini = new MiniServer(clientSocket);
	            mini.start();
			}	
			
		} catch (IOException ex){
			System.out.println("Could not listen on port: " + PORT);
			
		} finally{
			//close resources in finally block so that they get closed even if we hit an exception above
			try{
				if(serverSocket != null){
					serverSocket.close();
				}
				if(clientSocket != null){
					clientSocket.close();
				}
			} catch (IOException ex){
				ex.printStackTrace();
			}
		}
		System.out.println("server finished");
	}
}