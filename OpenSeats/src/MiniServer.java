import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.Socket;

public class MiniServer extends Thread{

    private Socket socket = null;
    ObjectOutputStream outputStream = null;
	ObjectInputStream inputStream = null;
	private static int clientIncrementer;

    public MiniServer(Socket socket) {
        super("MiniServer");
        this.socket = socket;
        clientIncrementer++;
    }

    public void run(){
		
		try {
			System.out.println("Client " + clientIncrementer + " connected");
			
			outputStream = new ObjectOutputStream(socket.getOutputStream());
			outputStream.flush();
			
			inputStream = new ObjectInputStream(socket.getInputStream());
			
			String clientCRN = (String) inputStream.readObject();
			System.out.println("Client " + clientIncrementer + " CRN: " + clientCRN);
			
			String clientEmail = (String) inputStream.readObject();
			System.out.println("Client " + clientIncrementer + " email: " + clientEmail);

			outputStream.writeObject("CRN and Email recieved. Loading...");
			outputStream.flush();
			
			WebScraper scraper = new WebScraper(clientCRN, clientEmail);
			String scrapeResults = scraper.scrape();
						
			outputStream.writeObject(scrapeResults);
			outputStream.flush();
			
			if(scraper.validCRN() == false){
				System.out.println("Client " + clientIncrementer + " inputted an INVALID CRN.");
			}
			else{
				File file = new File("/Users/kelemen/Documents/workspace/OpenSeatsNotifierPREimportedZIP/src/clientsData");
				try (BufferedWriter writer = new BufferedWriter(new FileWriter(file,true))) {
					writer.write(clientCRN + " " + clientEmail);
					writer.newLine();
				} catch (IOException e) {
					System.out.println("Problem writing to the file 'clientsData.txt'");
				}
				scraper.scrapeUntilOpenSeat();
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally{
			//close resources in finally block so that they get closed even if we hit an exception above
			try{
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