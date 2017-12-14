import javax.mail.Message;

import org.simplejavamail.email.Email;
import org.simplejavamail.mailer.Mailer;
import org.simplejavamail.mailer.config.TransportStrategy;

public class Mail {
	private final String userEmail;
	private final String classCRN;
	private final String className;
	private final String instructor;
	private final int openSeats;
	
	public Mail(String userEmail, String classCRN, String className, String instructor, int openSeats){
		this.userEmail = userEmail;
		this.classCRN = classCRN;
		this.className = className;
		this.instructor = instructor;
		this.openSeats = openSeats;
		setupMail();
	}
	
	public void setupMail(){
		
		String duckweblink = "https://duckweb.uoregon.edu/pls/prod/twbkwbis.P_WWWLogin";
		
		Email email = new Email();
        email.setFromAddress("Open Seats Notifier", "CRNavailability@gmail.com");
        email.setSubject(className + " has an open seat!");
        email.addRecipient("ClientUser", userEmail, Message.RecipientType.TO);
        email.setText("It's your lucky day! " + instructor + "'s " + className + " is open! Act fast!" + "\n" + duckweblink);

        new Mailer("smtp.gmail.com", 587, "CRNavailability@gmail.com", "yourboyblue22", TransportStrategy.SMTP_TLS).sendMail(email);
	}
	
}
