import java.util.*;

/**
 * Maintains an array of Referee objects
 * The array is ordered lexographically by referee ID
 * The methods allow objects to be added and deleted from the list
 * In addition the array can be returned in order of matches officiated 
 * SH: Please consult me on the sentence above at some point^^
 */

public class RefereeListing {

	//allRefs is the arraylist of referees used by the constuctor
	//temp is used in suggestRefs, so that suggestRefs can be called nondestructively
	private ArrayList<Referee> allRefs;
	private ArrayList<Referee> temp;

	//set of all senior level qualifications
	private final String[] SENIORQUALIF = new String[] {"NJB2", "NJB3", "NJB4", "IJB2", "IJB3", "IJB4"};
	private final Set<String> SENIORELIGIBILITY = new HashSet<String>(Arrays.asList(SENIORQUALIF));

	//set of all strings which signify willingness to travel to North
	private final String[] NORTHWILLINGNESS = new String[] {"YYY", "YYN", "YNN", "YNY"};
	private final Set<String> TRAVELNORTH = new HashSet<String>(Arrays.asList(NORTHWILLINGNESS));

	//set of all strings which signify willingness to travel to Central
	private final String[] CENTRALWILLINGNESS = new String[] {"YYY", "NYY", "YYN", "NYN"};
	private final Set<String> TRAVELCENTRAL = new HashSet<String>(Arrays.asList(CENTRALWILLINGNESS));

	//set of all strings which signify willingness to travel to South
	private final String[] SOUTHWILLINGNESS = new String[] {"YYY", "NYY", "YYN", "NNY"};
	private final Set<String> TRAVELSOUTH = new HashSet<String>(Arrays.asList(SOUTHWILLINGNESS));

	//toBeRemoved is filled with indices from an ArrayList<Referee> which are to be removed
	private ArrayList<Integer> toBeRemoved = new ArrayList<Integer>();

	//distances for the distance function
	private final int SAME_LOCATION = 0;
	private final int ADJACENT_LOCATIONS = 1;
	private final int NONADJACENT_LOCATIONS = 2;

	/* 
	 * Default constructor instanciates all an arraylist of referee objects
	 * 
	 */
	public RefereeListing()
	{
		allRefs = new ArrayList<Referee>();
	}

	/* 
	 * Constuctor to create a RefereeListing object from a given arraylist of referees
	 * 
	 */
	public RefereeListing(ArrayList<Referee> temp)
	{
		allRefs = new ArrayList<Referee>(temp);
	}

	/** this method is called by GUI and accepts attributes specific to each referee, 
	 * which is then sent as a Referee object...
	 * and is then added to the Referee List allRefs
	 */
	public void initRefList(String id, String name, String qualif, int alloc, String home, String travel){


		// Instantiate new Referee object and pass attributes
		Referee ref = new Referee(id, name, qualif, alloc, home, travel);

		// Allocate new Referee to list
		allRefs.add(ref);

	}

	/* returns referee at index i in the list */
	public Referee refAtIndex(int i)
	{
		Referee ref = null; 

		ref = allRefs.get(i);

		return ref;
	}

	//returns number of refs
	public int numRefs()
	{
		int numRefs = allRefs.size();
		return numRefs;
	}

	/* performs search on current list of referees and returns boolean, determining
	 * whether or not that referee exists.
	 * used for input validation, rather than returning Referee data.
	 */
	public boolean hasRef(String name){

		boolean found = false;

		for(int i=0; i<allRefs.size(); i++){

			//if the name of the referee is the same as the name input
			if(allRefs.get(i).getName().equals(name)){

				found = true;

				// match found, jump out of loop
				i=allRefs.size();				
			}
		}

		return found;

	}

	/* searches for specific name in allRefs list and returns that Referee object once match is found
	 * returns null if no match is found
	 * called by ManageFrame.editRef()
	 */
	public Referee searchRef(String name){

		Referee ref = null;

		for(int i=0; i<allRefs.size(); i++){

			if(allRefs.get(i).getName().equals(name)){
				// referee found, jump out of loop
				ref = allRefs.get(i);

			}
		}


		return ref;
	}

	/** 
	 *	Takes Referee details as an input and creates new Referee object which is assigned to List 	
	 * 
	 */
	public void addRef(String name, String qualif,int alloc, String home, String travel) {

		String id = "";

		// generate an ID for the new referee using idGenerator 
		id = this.idGenerator(name);

		// Instantiate new Referee with details and add to list
		Referee ref = new Referee(id, name, qualif, alloc, home, travel);

		// finaly, add this ref to allRefs
		allRefs.add(ref);

	}

	/* Removes a ref, provided the name matches.
	 * note: input validation in the GUI means that 
	 * name will always match if this method has been called.
	 */
	public void deleteRef(String name){

		for(int i = 0; i <allRefs.size(); i++){

			// Find ref in list
			if(allRefs.get(i).getName().equals(name)){
				// once match is found, remove
				allRefs.remove(i);
			}

		}

	}


	/*
	 * Deals with editRef functionality.
	 * Finds ref in list and uses setter methods in Referee to amend details
	 * note: uses all setter methods regardless of whether or not it has been changed
	 * GUI ensures correct data is inputted
	 */
	public void editRef(String name, String qualif, String home, String travel){

		Referee ref = null;

		for(int i=0; i<allRefs.size(); i++){

			//if the ith Ref in allRefs name is the same as the name input
			if(allRefs.get(i).getName().equals(name)){

				// Call setter methods to edit specified ref
				allRefs.get(i).setQualif(qualif);
				allRefs.get(i).setHome(home);
				allRefs.get(i).setTravel(travel);

				return;
			}
		}

	}

	/* this method generates an Id for a ref based on their name, assigns a number
	 * in the ID based on if their initials are shared with other refs or not
	 */
	public String idGenerator(String name){
		String id = "";
		String refID = "";
		String one = "1";

		//add the first initial to the ID
		id += name.charAt(0);

		//find the index of space between the names
		int space = name.indexOf(" ");

		// find the index of the second initial
		space++;
		int indexOfSurnameStart = space;

		//add the second initial to the ID
		id += name.charAt(indexOfSurnameStart);

		/* create a new variable refID so that id is not overwritten.
		 * If for example Dave Gray was in the list already but we try 
		 * to add David Gibson. This would cause the id to become DGDG1.
		 * This is because the first if statement was entered as DG=DG,
		 * however this was added to the orignal id String and therefore
		 * is now DG (original) plus the new id DG1. Using refID eliminates
		 * this problem.
		 */
		refID = id;
		for(int i = 0; i < allRefs.size(); i++){
			// if the initials in the ID match the initials in the IDs in the list
			if(id.substring(0,2).equals(allRefs.get(i).getRefID().substring(0,1)+allRefs.get(i).getRefID().substring(1,2))){

				// extract the last digit in the ID to increment this by 1
				int numberID = Integer.parseInt(allRefs.get(i).getRefID().substring(2));

				// get the incremented number into the new ID
				int endID = numberID+1;

				// add the number to the end of the ID				
				refID = id.substring(0,2)+endID;
			}

			/* if the initials have not appeared in an ID in the
			 * list then we need to add 1 to the ID.
			 */

			int lengthInitialsOnly = 2;
			if(refID.length() == lengthInitialsOnly){
				String fullID = id +one;
				refID = fullID.substring(0,3);
			}
		}	

		/* however if the arrayList allRefs is empty then we 
		 * want to add 1 to the initials of the entered name.
		 */ 
		int emptyRefList = 0;
		if(allRefs.size() == emptyRefList){
			refID += one;
		}

		return refID;
	}

	// this method allows the refs to be ordered by ID (lexicographical order)
	public void idSort(){
		Collections.sort(allRefs);
	}

	/**
	 * returns a list of all refs who are eligible for a match at some
	 * level (senior or junior) and location (north, central, or south)
	 */
	public RefereeListing suggestRefs(String level, String location)
	{
		temp = new ArrayList<Referee>(allRefs);

		//If the level of the match is senior get rid of all of all those refs who
		//can only referee junior level matches
		if (level.equals("Senior"))
		{
			for (int i = 0; i < temp.size(); i++)
			{
				//if SENIORELIGIBILITY doesn't contain this ref's qualification level
				if (!(SENIORELIGIBILITY.contains(temp.get(i).getQualif())))
				{
					temp.remove(i);
				}
			}

		}

		//add the index of refs to be removed to ArrayList toBeRemoved,
		//based on them not being willing to travel to the match location
		for (int i = 0; i < temp.size(); i++)
		{
			//if the location of the match is North and the referee is not willing to travel there
			if (location.equals("North") && !(TRAVELNORTH.contains(temp.get(i).getTravel())))
			{
				toBeRemoved.add(i);
			}
			//if the location of the match is Central and the referee is not willing to travel there
			else if (location.equals("Central") && !(TRAVELCENTRAL.contains(temp.get(i).getTravel())))
			{
				toBeRemoved.add(i);
			}
			//if the location of the match is South and the referee is not willing to travel there
			else if (location.equals("South") && !(TRAVELSOUTH.contains(temp.get(i).getTravel())))
			{
				toBeRemoved.add(i);
			}
		}

		//sort toBeRemoved in reverse order, since removing objects from an array list 
		//in reverse order is the safe way to do so `
		Collections.sort(toBeRemoved, Collections.reverseOrder());

		//for each index in toBeRemoved, now remove it from temp
		for (int i: toBeRemoved)   
		{
			temp.remove(i);
		}

		//sort first by distance, to favor refs close to the match, then by allocation
		sortByDistance(location);
		sortByAllocation(location);

		//create a RefereeListing object based on temp
		RefereeListing tempAsRefList = new RefereeListing(temp);

		//cleartoBeRemoved to be ready for future suggestions
		toBeRemoved.clear();

		return tempAsRefList;
	}

	/* from two input strings (the location of the ref and the location of the match)
	 *  distance() returns 
	 * 		- 0, if they are the same location
	 *		- 1, if they are adjacent locations
	 *		- 2, if they are nonadjacent locations
	 */
	public int distance(String refLoc, String matchLoc)
	{
		int dist = 0;

		//if the ref's location and the match locaton are the same
		if (refLoc.equals(matchLoc))
		{
			dist = SAME_LOCATION;	
		}
		//else if the ref's location and the match location are adjacent
		else if ((refLoc.equals("North") && matchLoc.equals("Central")) 
				|| (refLoc.equals("Central") && matchLoc.equals("North"))
				|| (refLoc.equals("Central") && matchLoc.equals("South"))
				|| (refLoc.equals("South") && matchLoc.equals("Central")))
		{
			dist = ADJACENT_LOCATIONS;
		}
		//else if the ref's location and the match location are nonadjacent
		else if ((refLoc.equals("North") && matchLoc.equals("South")) 
				|| (refLoc.equals("South") && matchLoc.equals("North")))
		{
			dist = NONADJACENT_LOCATIONS;
		}

		return dist;
	}

	/*sort based on distance (to favor refs in the same location as the match,
	 * then adjacent locations, then non-adjacent)
	 * using insertion sort
	 */
	private void sortByDistance(String location)
	{
		//temp refs required when swapping during sort
		Referee tempRef1;
		Referee tempRef2;

		//j used to loop through all previous elements of i (i is instanciated in the loop)
		int j;

		for (int i = 1; i < temp.size(); i++)
		{
			j = i;

			//while j is positive and (j-1) is further away from the match location than j
			while ((j > 0)
					&& (distance(temp.get(j - 1).getHome(), location) > distance(temp.get(j).getHome(), location)))
			{
				//swap refs at (j-1) and j
				tempRef1 = temp.get(j - 1);
				tempRef2 = temp.get(j);

				temp.set(j, tempRef1);
				temp.set(j - 1, tempRef2);

				//decrement j
				j = j - 1;
			}
		}
	}

	/*
	 * sort based on allocation in areas equidistant to the match location
	 * using insertion sort
	 */
	private void sortByAllocation(String location)
	{
		//temp refs required when swapping during sort
		Referee tempRef1;
		Referee tempRef2;

		//j used to loop through all previous elements of i (i is instanciated in the loop)
		int j;
		for (int i = 1; i < temp.size(); i++)
		{
			j = i;

			//while j is positive and (j-1) has more allocations than j
			while ((j > 0)
					&& (temp.get(j - 1).getAlloc() > temp.get(j).getAlloc()))
			{
				//if refs (j-1) and j are in the same location
				if (distance(temp.get(j - 1).getHome(), location) == distance(temp.get(j).getHome(), location))
				{
					//swap refs at (j-1) and j
					tempRef1 = temp.get(j - 1);
					tempRef2 = temp.get(j);

					temp.set(j, tempRef1);
					temp.set(j - 1, tempRef2);

				}

				//decrement j
				j = j - 1;
			}
		}
	}

}
