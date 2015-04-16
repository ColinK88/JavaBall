public class Referee implements Comparable<Referee> {

	/** Instance Variables **/
	private String refID;
	private String refName;
	private String qualification;
	private int allocations;

	private String home;

	// Stores which areas each Referee is willing to travel to.
	// MUST include home locality
	private String travel;

	/* Default Constructor - Accepts referee attributes. */
	public Referee(String id, String name, String qualif, int alloc, String h, String trav) {
		this.refID = id;
		this.refName = name;
		this.qualification = qualif;
		this.home = h;
		this.allocations = alloc; 
		// Travel willingness to each locality stored as string
		// e.g. yny = north(yes), central(no), south(yes)
		this.travel = trav;

	}

	/* Setter methods for updating/editing referee details */
	public void setQualif(String qualif) {	this.qualification = qualif; }
	public void setHome(String h) { this.home = h; }
	public void setAlloc(int a){ this.allocations = a; }
	public void setTravel(String trav) { this.travel = trav; }

	/*
	 * Call this method everytime a refree is allocated to a match
	 * To increment their allocation by 1.
	 */
	public void incrementAllocation()
	{
		allocations++;
	}

	/* compareTo on String objects automatically returns lexicographical order
	 * Modified to order by Referee ID's.
	 *
	 */
	public int compareTo(Referee other) 
	{
		return this.getRefID().compareTo(other.getRefID());
	}

	/** Accessor methods **/
	public String getRefID() 
	{
		return refID; 
	}

	public String getName() 
	{ 
		return refName; 
	}

	public String getQualif() 
	{ 
		return qualification; 
	}

	public String getHome() 
	{ 
		return home; 
	}

	public int getAlloc() 
	{ 
		return allocations; 
	}

	public String getTravel() 
	{ 
		return travel; 
	}


	public String getPretty()
	{
		String pretty = getRefID() + " " + getName() + " " + getQualif() + " " + getAlloc() + " " + getHome() + " " + getTravel();

		return pretty;
	}
}
