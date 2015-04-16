import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * This class maintains a list of Match objects and can be used to access match information.	
 * GUI initialises this. 
 */

public class MatchSeason {

	//an arraylist of Match objects																													
	private ArrayList<Match> season;
	private final int MAXMATCHES = 52;

	//default constructor
	public MatchSeason(){ 

		season = new ArrayList<Match>();  

	}

	/** 
	 * insert a MatchClass object to the list
	 */
	public void addMatch(Match newMatch){
		season.add(newMatch);
	}

	/**  
	 * Creates the String for the MatchAllocs report that is created when Save and Exit button is pressed.
	 * 
	 */ 
	public String getMatchReport(){

		//Heading of the report
		String heading = "Match Allocations Report";

		//into info we will add the matchInfo of each Match in Season, on its own line
		String info = "";

		for (int i = 0; i < season.size(); i++){
			String nextLine = season.get(i).matchInfo();
			info = info + "\n" + nextLine;
		}

		//Concatenate heading and info strings
		String masterString = heading + "\n" + info;
		return masterString;

	}

	/*
	 * Takes a match week as input and determines whether a match has already...
	 * been scheduled for that week.
	 * Uses linear search
	 * @return boolean - whether match has been scheduled for specified week
	 */
	public boolean duplicateMatch(String w){
		// Boolean to be returned
		boolean duplicate = true;
		// parse string to integer for comparison
		int compareWeek = Integer.parseInt(w);
		// Week to be compared
		int week;

		// Only do search if season containts atleast 1 match
		// i.e. if ArrayList is greater than size 0
		if(season.size() > 0){
			// Search through season ArrayList and look for duplicate week
			for(int i=0;i<season.size();i++){

				// retrieve week from Match.class at position i in list
				// parse to int
				week = Integer.parseInt(season.get(i).getWeek());
				// compare input and match week
				if(week == compareWeek)
				{
					// if match is found, set boolean to false
					duplicate = false;
					// jump out of loop
					i=season.size();
				}

			}
		}

		return duplicate;

	}


}	