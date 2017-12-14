import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Document;

public class WebScraper {
	private Document classPage;
	private final String userCRN;
	private final String userEmail;
	private String _className;
	private String _instructor;
	private String _classCRN;
	private int _openSeats;
	private Boolean _validCRN;
	
	public WebScraper(String userCRN, String userEmail){
		this.userCRN = userCRN;
		this.userEmail = userEmail;
	}
	
	public String scrape(){
		String scrapeSummary = "";
		try {
			classPage = Jsoup.connect("https://duckweb.uoregon.edu/pls/prod/hwskdhnt.p_viewdetl?term=201701&crn=" + userCRN).timeout(0).get();
		
			//dddefault is a class in the url's html. The # of open seats & CRN are of the dddefault class.
			List<String> dddefaults = new ArrayList<>();
	
			for( Element element : classPage.select("TD[class=dddefault]") )
			{
			    dddefaults.add(element.text());
			}
			
			//dddead is a class in the url's html. The class name and instructor are of the dddead class.
			List<String> dddeads = new ArrayList<>();
	
			for( Element element : classPage.select("TD[class=dddead]") )
			{
			    dddeads.add(element.text());
			}
			
			if(dddefaults.size() == 0 || dddeads.size() == 0){
				_validCRN = false;
				return("Invalid CRN. Please enter a valid CRN.");
			}
			else{
				_validCRN = true;
			}
			
			_className = dddeads.get(0).replaceAll("\u00A0", "");
			_instructor = dddeads.get(7);
			
			_classCRN = dddefaults.get(1);
			_openSeats = Integer.parseInt(dddefaults.get(2));
			
			scrapeSummary += ("CRN: " + _classCRN + "\n" + _className + "\n" + _instructor + "\n" + "Slots open: " + _openSeats + "\n");
			
			if(_openSeats < 1){
				scrapeSummary += ("Unfortunately there are no open seats at the moment. We will email you when there is one available.");
			}
			else{
				scrapeSummary += ("Your class has an opening right now!");
			}
	
		} catch (IOException e) {
			e.printStackTrace();
		}
		return scrapeSummary;
			
	}
	public Boolean validCRN(){
		return _validCRN;
	}
	
	public String getClassName(){
		return _className;
	}
	
	public String getInstructor(){
		return _instructor;
	}
	
	public int getOpenSeats(){
		return _openSeats;
	}
	
	public void scrapeUntilOpenSeat(){
		int seatPings = 0;
		
		if(_openSeats == 0){
			System.out.println("waiting for an open seat... any day now");
			System.out.println("0\n1\n2\3\n...");
		}
		while(_openSeats == 0){
			//is this really looking at a new _openSeats every time?? Or the same one?
			scrape();
		}
		
		if(_openSeats > 0){
			Mail mail = new Mail(userEmail, _classCRN, _className, _instructor, _openSeats);
		}
		
	}
}