/**
 * Match creates individual match objects which will be stored in MatchSeason as an ArrayList.
 */
public class Match{

	//the week number in which the match takes place
	private String week;	

	//the location of the match								
	private String location;

	//the two referees assigned to the match
	private Referee referee1;
	private Referee referee2;

	//the level of the match (junior or senior)
	private String level;

	/**
	 * default constructor
	 * 
	 */
	public Match(String wk, String loc, Referee ref1, Referee ref2, String lvl){
		week = wk;				
		location = loc;
		referee1 = ref1;			
		referee2 = ref2;			
		level = lvl;
	}

	/**
	 * Accessor methods
	 */
	public String getWeek() { return week; }
	public String getArea() { return location; }
	public String getRef1() { return referee1.getName(); } //returns ref1's name
	public String getRef2() { return referee2.getName(); } //returns ref2's name
	public String getLevel() { return level; }


	/** 
	 * This method returns a formatted string for each match for the MatchAllocs report 
	 * Format: 23 Senior North 		Derek Riordan	Jane Gray
	 * This string will be called from MatchSeason for the report. 
	 */ 
	public String matchInfo(){

		String info = String.format("%-13s %-13s %-13s %-20s %-20s", week, level, location, getRef1(), getRef2());

		return info;
	}
}