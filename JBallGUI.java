import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;

@SuppressWarnings("serial") 

public class JBallGUI extends JFrame implements ActionListener{

	/** GUI JButtons */
	private JButton saveExitButton, visualStatsButton;
	private JButton manageRefButton, allocateRefButton, searchRefButton;

	/** GUI textfields */
	private JTextField matchWeekIn, matchLvlIn, matchLocIn;
	private JTextField allocTextField, refNameTextField, searchTextField;

	// Combo boxes
	private JComboBox<String> refInfo, matchLevel, matchLoc;

	/** GUI JLabels */
	private JLabel week, location, level, search;

	/** Referee list display */
	private JTextArea refDisplay, searchTextArea, allocationTextArea, refsSuitable;

	/** JFrames */
	private JFrame mainGUIDisplay, editRefDisplay, searchRefDisplay, openBarChart;

	/** JPanels */
	private JPanel searchPanel, buttonPanel, matchInfo;
	private JPanel center, refListSuitable;

	/** manage equals null is used when opening manage (guards against opening multiple windows) */
	private ManageFrame manage = null;

	/** Season is an arrayList of matches */
	private Match match;
	private MatchSeason season = new MatchSeason(); 

	/** referee listing object, Instantiates single instance for use of entire JBallGUI class.*/
	private RefereeListing refereeListing;
	private RefereeListing eligibleRefs;

	/** display of bar chart - used when visual statistics button is pressed */
	private BarChart chart = null;

	/** names of input text files */
	private final String REF_IN_FILE = "RefereesIn.txt";
	private final String REF_OUT_FILE = "RefereesOut.txt";
	private final String MATCH_OUT_FILE = "MatchAllocs.txt";

	/** first and last weeks used in match validation */
	private final int FIRST_WEEK = 1;
	private final int LAST_WEEK = 52;

	/** used in allocation() to ensure that there are enough eligible refs for the match to be allocated */
	private final int REFS_PER_MATCH = 2;

	// Converts lower case chars to upper case.
	private final int TOUPPERCASE = 32;	


	/** match is allocated to the first and second referees on the list */
	private final int FIRST_REF = 0;
	private final int SECOND_REF = 1;




	/** Constructor for GUI */
	public JBallGUI(){

		// spec for main JFrame
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setTitle("Java Ball Referees");
		setSize(770,680);
		setMinimumSize(new Dimension(770,270));
		setResizable(true);

		// refDisplay JTextArea spec and instantiation
		refDisplay = new JTextArea();
		refDisplay.setFont(new Font("Courier", Font.PLAIN, 14));
		refDisplay.setSize(760,300);
		refDisplay.setEditable(false);

		// refsSuitable JTextArea spec and instantiation
		refsSuitable = new JTextArea();
		refsSuitable.setFont(new Font("Courier", Font.PLAIN, 14));
		refsSuitable.setSize(400, 300);
		refsSuitable.setVisible(false);
		refsSuitable.setEditable(false);

		// add refDisplay to the main JFrame
		add(refDisplay, BorderLayout.NORTH);

		// these helper methods are used to layout the GUI 	
		this.layoutTop();
		this.layoutCenter();
		this.layoutBottom();

		this.readRefInFile();
	}

	// helper method to layout the north of the GUI 
	public void layoutTop(){
		// instantiate the center JPanel 
		center = new JPanel();

		// need to add the search items to the searchPanel
		search = new JLabel("Referee Name: ");

		// assumption that name !> 25 characters 
		searchTextField = new JTextField(25);

		searchRefButton = new JButton("Search Referee");

		// add action listener to the search button
		searchRefButton.addActionListener(this);

		// make JComboBox for choice of info to be displayed
		refInfo = new JComboBox<String>();
		// add items to combo box
		refInfo.addItem("Select");
		refInfo.addItem("ID");
		refInfo.addItem("Qualification");
		refInfo.addItem("Match Allocations");
		refInfo.addItem("Home Locality");
		refInfo.addItem("Travel Willingness");
		refInfo.addItem("All Details");
		// cannot be editable
		refInfo.setEditable(false);

		// add components to the center panel
		center.add(search);
		center.add(searchTextField);
		center.add(refInfo);
		center.add(searchRefButton);
	}

	// helper method to layout the center of the GUI 
	public void layoutCenter(){	
		/* instantiate and add JPanels, JLabels, JTextFields
		 * JComboBoxes 
		 */

		matchInfo = new JPanel();

		week = new JLabel("Match Week: ");
		// week - max 2 - 1-52
		matchWeekIn = new JTextField(2);

		level = new JLabel("Match Level: ");

		matchLevel = new JComboBox<String>();
		// add items to the combo box
		matchLevel.addItem("Select");
		matchLevel.addItem("Junior");
		matchLevel.addItem("Senior");

		location = new JLabel("Match Location: ");

		matchLoc = new JComboBox<String>();
		// add items to the combo box		
		matchLoc.addItem("Select");		
		matchLoc.addItem("North");
		matchLoc.addItem("Central");
		matchLoc.addItem("South");

		allocateRefButton = new JButton("Allocate Referees");
		// add action listener to the allocate button
		allocateRefButton.addActionListener(this);

		// add the JComponents to the panel 
		matchInfo.add(week);
		matchInfo.add(matchWeekIn);
		matchInfo.add(level);
		matchInfo.add(matchLevel);
		matchInfo.add(location);
		matchInfo.add(matchLoc);
		matchInfo.add(allocateRefButton);

		// add the matchInfo panel to the center panel 
		center.add(matchInfo, BorderLayout.NORTH);

		refListSuitable = new JPanel();
		// add the refListSuitable panel to the center panel
		center.add(refListSuitable, BorderLayout.SOUTH);

		// add center panel to the JFrame
		add(center, BorderLayout.CENTER);
	}

	// sets the layout for the bottom of the GUI 
	public void layoutBottom(){
		JPanel south = new JPanel();

		manageRefButton = new JButton("Manage Referees");
		manageRefButton.addActionListener(this);

		saveExitButton = new JButton("Save and Exit");
		saveExitButton.addActionListener(this);

		visualStatsButton = new JButton("Visual Statistics");
		visualStatsButton.addActionListener(this);

		// add buttons to south panel
		south.add(manageRefButton);
		south.add(visualStatsButton);
		south.add(saveExitButton);

		add(south, BorderLayout.SOUTH);

	}

	/* this method is used to read in the RefereesIn.txt file */
	public void readRefInFile(){
		String refInContents = "";
		String fullContent = "";
		try{
			FileReader fileReadRefsIn = null;
			Scanner refsInScanner = null;

			try{
				// create new scanner and fileReader objects 
				fileReadRefsIn = new FileReader(REF_IN_FILE);
				refsInScanner = new Scanner(fileReadRefsIn);

				refereeListing = new RefereeListing();

				/* while there exits a next line in the file we 
			 	can continue through the loop */
				while(refsInScanner.hasNextLine()){
					refInContents = refsInScanner.nextLine();
					/* here we will need to send each line to the 
					part of the refereeListings class to be split and 
					worked with */
					this.addReferee(refInContents);
				}
			}
			finally{
				/* fileReader and scanner need to be closed here */
				if(fileReadRefsIn != null){
					fileReadRefsIn.close();
				}
				if(refsInScanner != null){
					refsInScanner.close();
				}
			}
		}
		catch(IOException exception){
			JOptionPane.showMessageDialog(this, "File Not Found", 
					"File Not Found", JOptionPane.ERROR_MESSAGE);
		}

		// update display JTextArea after the file has been read in  
		updateDisplay();
	}

	/* this method adds a referee to the arrayList in refereeListing */
	public void addReferee(String refInContents){
		String [] refTokens = refInContents.split(" +");
		// splits the ID from referee details 
		String id = refTokens[0];

		// split the name 
		String name = refTokens[1]+" "+refTokens[2];

		// split the qualification part
		String qualification = refTokens[3];

		//split the allocation 
		//
		String allocationAsString = refTokens[4];

		// need to parse the allocations to an integer
		int allocation = Integer.parseInt(refTokens[4]);

		//split the home locality part
		String home = refTokens[5];

		//split travel willingness
		String travel = refTokens[6];

		// Sends tokenised String to RefereeListing to be populate Referee List
		refereeListing.initRefList(id, name, qualification, allocation, home, travel);

	}

	/* this method prints to the JTextArea "display" the list of referees */
	public String updateDisplay()
	{
		//clear the display of previously entered text
		refDisplay.setText("");

		//instanciate a string which be added to to become the displayed text
		String displayText = "";

		int columnWidth = 16; //need to make a finaal int for this

		String header = alignColumns("RefID", columnWidth) + alignColumns("Name", columnWidth) + 
				alignColumns("Qualification", columnWidth) + alignColumns("Allocation", columnWidth) + 
				alignColumns("Home", columnWidth) + alignColumns("Travel", columnWidth);


		String separator = "";
		for(int i=0; i < header.length(); i++){
			separator += "-";
		}		

		//add the first line of the display, the title
		displayText = displayText + header + "\n";

		// add the separator line
		displayText = displayText + separator + "\n";

		refereeListing.idSort();

		for (int i  = 0; i < refereeListing.numRefs(); i++)
		{
			Referee ref = refereeListing.refAtIndex(i);

			//to sort the referees in ID order

			displayText = displayText + alignColumns(ref.getRefID(), columnWidth);

			displayText = displayText + alignColumns(ref.getName(), columnWidth);

			displayText = displayText + alignColumns(ref.getQualif(), columnWidth);

			String alloc = "" + ref.getAlloc();

			displayText = displayText + alignColumns(alloc, columnWidth);

			displayText = displayText + alignColumns(ref.getHome(), columnWidth);

			displayText = displayText + alignColumns(ref.getTravel(), columnWidth);

			displayText = displayText + "\n";


		}

		refDisplay.append(displayText);

		return displayText;
	}

	/**
	 * adds spaces in the display to align columns correctly
	 * inputs are a string which is the word in the column and an int
	 */
	public String alignColumns(String word, int columnWidth)
	{
		//get the length of the word
		int wordLength = word.length();

		//for each k in the closed interval [wordLength, columnWidth], add a space
		for (int k = wordLength; k <= columnWidth; k++)
		{
			word = word + " ";
		}

		return word;

	}

	// opens the instance of manage frame (manage)
	public void manageRef(){

		//only open the manageFrame window if it isn't already open (that is it is set to null)
		if (manage == null)
		{
			manage = new ManageFrame(refereeListing, this);
		}
	}

	//manageNull allows us to set manage equal to null, called from ManageFrame when closing the window
	public void manageNull()
	{
		manage = null;
	}

	public void searchRef(){
		/* this method will call the method within refereeListing 
		 * that performs a search on the referee ID's. The matching
		 * info will be displayed to the user via a pop up window.
		 */

		/* need to create a new pop up window. This window will display
		 * a string including the info that matches the input name.
		 */

		searchRefDisplay = new JFrame();
		searchRefDisplay.setSize(300, 200);

		// Set initial position of window so that there is no window overlap
		searchRefDisplay.setLocation(800, 300);

		// pull name from text field, call nameFormat() and store as string
		String nameEntered = this.nameFormat(searchTextField.getText().trim());

		// Check for input validation - empty fields, no search match 
		if(nameEntered.isEmpty()){
			// empty field
			JOptionPane.showMessageDialog
			(null, "Field cannot be empty", "Warning", JOptionPane.ERROR_MESSAGE);

			// set the combo box back to default setting "Select"
			resetFields();
		}
		// check for numerical characters in string
		else if(this.checkNumericalInput(nameEntered)){
			// display message warning
			JOptionPane.showMessageDialog
			(null, "This field cannot contain numbers", "Warning", JOptionPane.ERROR_MESSAGE);

			resetFields();
		}
		// Check if referee exists
		else if(!refereeListing.hasRef(nameEntered)){
			// no match
			JOptionPane.showMessageDialog
			(null, "No referee found", "Warning", JOptionPane.ERROR_MESSAGE);

			resetFields();

		}
		// Ensure search option has been chosen
		else if(refInfo.getSelectedItem().equals("Select")){
			JOptionPane.showMessageDialog
			(null, "Please select a search option", "Warning", JOptionPane.ERROR_MESSAGE);
		}
		else{
			searchRefDisplay.setTitle("Referee Details: "+ nameEntered);

			String title = "";

			// create a new object of refereeListing
			Referee ref = refereeListing.searchRef(nameEntered);		

			// build the string dependant on what is chosen on the drop down menu
			if(refInfo.getSelectedItem().equals("ID")){
				title += "ID: "+ref.getRefID();
			}

			else if(refInfo.getSelectedItem().equals("Qualification")){
				title += "Qualification: "+ref.getQualif();
			}

			else if(refInfo.getSelectedItem().equals("Match Allocations")){
				title += "Match Allocations: "+ref.getAlloc();
			}

			else if(refInfo.getSelectedItem().equals("Home Locality")){
				title += "Home Locality: "+ref.getHome();			
			}

			else if(refInfo.getSelectedItem().equals("Travel Willingness")){
				title += "Travel Willingness: "+ref.getTravel();
			}

			else if(refInfo.getSelectedItem().equals("All Details")){
				title += "ID: "+ref.getRefID() + "\n"+ "Qualification: "+ref.getQualif()+"\n"
						+"Match Allocations: "+ ref.getAlloc() + "\n" + "Home Locality: "+
						ref.getHome() + "\n" + "Travel Willingness: "+ref.getTravel()+ "\n";
			}

			// add the string we have just created to a textArea
			searchTextArea = new JTextArea();
			searchTextArea.append(title);
			searchTextArea.setEditable(false);

			// add the textArea to the JFrame and make it visible
			searchRefDisplay.add(searchTextArea, BorderLayout.CENTER);
			searchRefDisplay.setVisible(true);

			//reset
			resetFields();
		}
	}

	// this method is to display the bar chart
	// need to access the bar chart class.
	//only open the BarChart window if it isn't already open (that is it is set to null)
	public void displayChart(){
		if (chart == null)
		{
			chart = new BarChart("Referee Statistics", "Number of Matches refereed", refereeListing, this);
			chart.pack();  //makes the chart fit the window             
			chart.setVisible(true); //sets it visible
		}
	}

	//this method is called when the chart is closed so that it can be opened again	
	public void displayChartNull() 
	{								
		chart = null;
	}

	//creates a RefereeListing object of all suitable referees
	public void allocation(){

		// read the following from the text fields
		// keep week as a string as we will be using it in a string
		String week = matchWeekIn.getText().trim();
		String lvl = (String)matchLevel.getSelectedItem();
		String loc = (String)matchLoc.getSelectedItem();

		// if the information supplied in the GUI passes validation
		if(matchValidation() == true){

			// create eligibleRefs from all those refs in refereeListing eligible to referee 
			// a match of some level in some locations
			eligibleRefs = refereeListing.suggestRefs(lvl,loc);

			// if there are at least two referees in eligibleRefs
			if (eligibleRefs.numRefs() >= REFS_PER_MATCH)
			{
				// record the allocations of the referees before incrementing them
				int firstRefPrevAllocation = eligibleRefs.refAtIndex(FIRST_REF).getAlloc();
				int secondRefPrevAllocation = eligibleRefs.refAtIndex(SECOND_REF).getAlloc();

				// incrementing in eligibleRefs also increments in refereeListing
				eligibleRefs.refAtIndex(FIRST_REF).incrementAllocation();
				eligibleRefs.refAtIndex(SECOND_REF).incrementAllocation();

				// display these referees on the GUI - send to refAllocations
				refAllocations(eligibleRefs, lvl, loc, firstRefPrevAllocation, secondRefPrevAllocation);
				// allow this display to be seen 
				refListSuitable.setVisible(true);

				//create a new match object and add it to season	
				match = new Match(week, loc, eligibleRefs.refAtIndex(FIRST_REF), eligibleRefs.refAtIndex(SECOND_REF), lvl);	
				season.addMatch(match);

				updateDisplay();

				// reset fields once the allocation is performed	
				resetFields();

			}
			else {
				JOptionPane.showMessageDialog(this, "Two suitable referees cannot be found to referee a "+lvl+" match in "+loc+" in week "+week, "Allocation Error", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	/* this method builds a string containing the allocated and 
	 * eligible referees and displays this on a text area
	 */
	public void refAllocations(RefereeListing eligibleRefs, String lvl, String loc, int firstRefAlloc, int secondRefAlloc){
		refsSuitable.setText("");

		int firstEligible = 0;
		int secondEligible = 1;

		String displayOfEligible = "";
		// originally make the JPanel invisible
		refListSuitable.setVisible(false);

		// leave this as a string rather than an int here
		// because we will be using the info in a string
		String week = matchWeekIn.getText().trim();
		// build up the string 

		// adding the level entered 
		displayOfEligible += lvl; 

		displayOfEligible +=" match on week ";

		// add on the week entered 
		displayOfEligible += week;

		displayOfEligible += " in ";

		// add on the location entered
		displayOfEligible += loc;

		displayOfEligible += " will be refereed";

		// new line 
		displayOfEligible += "\nby ";

		// add on the first eligible ref 
		String nameFirst = eligibleRefs.refAtIndex(FIRST_REF).getName();
		displayOfEligible += nameFirst;

		// add on the first eligible refs updated allocation 
		displayOfEligible += " with updated allocation of ";
		displayOfEligible += refereeListing.searchRef(nameFirst).getAlloc();

		displayOfEligible += " and \n";

		// add on the second eligible ref
		String nameSecond = eligibleRefs.refAtIndex(SECOND_REF).getName();
		displayOfEligible += nameSecond;

		// add on the second eligible refs updated allocation 
		displayOfEligible += " with updated allocation of ";
		displayOfEligible += refereeListing.searchRef(nameSecond).getAlloc() +".";

		// add two new lines
		displayOfEligible += "\n"+"\n";

		int titleColumnWidth = 30; 

		// make a string containing the titles 
		String title = "";
		title += alignColumns("Eligible Referees", titleColumnWidth);
		title += "Previous Allocations"+"\n";

		// make a separator line which is the length of the title string
		String separator = "";
		for(int i = 0; i < title.length(); i++){
			separator += "-";
		}

		/* add both the title string and the separator line to the 
		 * string to display 
		 */
		displayOfEligible += title + separator + "\n";
		String refNameAlloc = "";
		String allocated = "";
		int columnWidth = 40;

		/* the next for loop allow us to carry out different 
		 * steps dependant on the size of the arrayList
		 */

		for(int i = 0; i < eligibleRefs.numRefs(); i++){
			refNameAlloc += alignColumns(eligibleRefs.refAtIndex(i).getName(), columnWidth);

			//if ref at i is the first in the list, add their previous allocations instead of their current
			if (i == FIRST_REF)
			{
				refNameAlloc += firstRefAlloc +"\n";
			}
			//if ref at i is the second in the list, add their previous allocations instead of their current			
			else if (i ==SECOND_REF)
			{
				refNameAlloc += secondRefAlloc +"\n";
			}
			else
			{
				refNameAlloc += eligibleRefs.refAtIndex(i).getAlloc()+"\n";
			}
		}	

		// add the name and alloc of all the eligible refs 
		displayOfEligible += refNameAlloc;

		// add the string to the textArea refsSuitable and set visible
		refsSuitable.append(displayOfEligible);
		refsSuitable.setVisible(true);

		// add to the JPanel refListSuitable
		refListSuitable.add(refsSuitable, BorderLayout.CENTER);

	}

	/*
	 * This method checks for numerical characters contain in any input string
	 * used for input validation
	 * @return found - whether numerical character is contained in string
	 */
	public boolean checkNumericalInput(String name){

		boolean found = false;

		// check for non numerical input
		// by looping through each letter and running String.matches()
		// and checking for any numerical input
		for(int i=0;i<name.length();i++){

			// extract letter at position i in String
			// charAt returns char so it must be casted back to String
			String letter = ""+name.charAt(i);

			// if numerical character is found...
			if(letter.matches("^\\d")){

				// clear fields
				resetFields();
				// jump out of loop at first occurrence.
				i=name.length();
				found = true;
			}
		}

		return found;
	}

	/** Validations 
	 * Match validation.
	 * Tests all fields for valid input and returns 'true' if test passes
	 */
	public boolean matchValidation(){ // my version

		// Take match week and store as String
		String weekInString = matchWeekIn.getText().trim();

		// Used to test for input validation
		// Will remain false if all tests below fail.
		boolean proceedAlloc =false;


		int weekIn = 0;
		// Handle invalid input, non-numerical characters, before parsing to int
		try{
			weekIn = Integer.parseInt(weekInString); 
		}catch(NumberFormatException e){
			// Set weekIn to -1 so it will be purposely caught by input validation below
			weekIn = -1;
		}

		// Following conditional tests for invalid input on all match fields.
		// proceedAlloc will only be true if all tests pass. 
		if(weekInString.isEmpty()){
			// empty message
			JOptionPane.showMessageDialog(null, "Match field cannot be left empty.", 
					"Match Level Error", JOptionPane.ERROR_MESSAGE);
		}
		else if(weekIn < FIRST_WEEK || weekIn > LAST_WEEK){
			// invalid week
			JOptionPane.showMessageDialog(null, "The week must be between 1 and 52", 
					"Match Level Error", JOptionPane.ERROR_MESSAGE);
			// clear field
			matchWeekIn.setText("");
		}
		else if(matchLevel.getSelectedItem().equals("Select")){
			// unselected dropdown
			JOptionPane.showMessageDialog(null, "Please select a level option from the drop down list", 
					"Match Level Error", JOptionPane.ERROR_MESSAGE);
		}
		else if(matchLoc.getSelectedItem().equals("Select")){
			// unselected dropdown
			JOptionPane.showMessageDialog(null, "Please select a location option from the drop down list", 
					"Match Location Error", JOptionPane.ERROR_MESSAGE);
		}
		else if(!season.duplicateMatch(weekInString)){
			// already allocated
			JOptionPane.showMessageDialog(null, "A match has already been allocated for this week. Please try another", 
					"Match Location Error", JOptionPane.ERROR_MESSAGE);
		}
		else{
			proceedAlloc = true;
		}

		return proceedAlloc;
	}

	/* Formats all inputted name strings to ensure first character of first/last name are uppercase */
	public String nameFormat(String nameIn){

		// output string
		String nameOut = "";

		// Find index of first character of second name(index of first name is alway 0)
		int secondName = nameIn.indexOf(" ")+1;

		// Convert to character array to do replace operations
		char []charName = nameIn.toCharArray();
		int charCheck = (int)charName[0];

		// if first character is lowercase..
		if(charCheck >= 'a' && charCheck <= 'z'){

			// Replace first character of first name and make uppercase
			charName[0] = (char)(charName[0] -TOUPPERCASE);
		}

		// set character to be changed to first char of second name
		charCheck = (int)charName[secondName];

		// if first character is lower case...
		if(charCheck >= 'a' && charCheck <= 'z'){
			// Replace first character of second name and make uppercase
			charName[secondName] = (char)(charName[secondName] -TOUPPERCASE);
		}


		// Convert char array back to string
		for(int i=0; i<charName.length;i++){
			nameOut += charName[i];
		}
		// return formatted name
		return nameOut;
	}


	public void resetFields()
	{
		// Reset search JComponents to default
		searchTextField.setText("");
		refInfo.setSelectedItem("Select");

		// Reset match JComponenst to default
		matchWeekIn.setText("");
		matchLoc.setSelectedItem("Select");
		matchLevel.setSelectedItem("Select");
	}

	/* Here we want to create a file named RefereesOut.txt and save the current
	 * list of referees 
	 */
	public void saveAndExit(){
		try{
			PrintWriter fileWriterRefsOut = null;
			fileWriterRefsOut = new PrintWriter(REF_OUT_FILE);
			try{
				/* here we want to write the String holding all the referees
				 * who have been allocated to matches  
				 */
				fileWriterRefsOut.write(updateDisplay());
			}
			finally{
				//need to close the fileWriter as long as it was opened originally
				if(fileWriterRefsOut != null){
					fileWriterRefsOut.close();
				}
			}
		}
		/* we also need an IOException in the case where the file was not found */
		catch(IOException e){ 
			JOptionPane.showMessageDialog(this, "Error", "File Not Found!", JOptionPane.ERROR_MESSAGE);
		}

		// need to write match output to a file 
		try{
			PrintWriter fileWriterMatchOut = null;
			fileWriterMatchOut = new PrintWriter(MATCH_OUT_FILE);
			try{
				/* here we want to write the String holding all the referees
				 * who have been allocated to matches  
				 */
				fileWriterMatchOut.write(season.getMatchReport());
			}
			finally{
				//need to close the fileWriter as long as it was opened originally
				if(fileWriterMatchOut != null){
					fileWriterMatchOut.close();
				}
			}
		}
		/* we also need an IOException in the case where the file was not found */
		catch(IOException e){ 
			JOptionPane.showMessageDialog(this, "Error", "File Not Found!", JOptionPane.ERROR_MESSAGE);
		}
		/* as we want the program to exit when the file is written to 
		 * we need this command for the GUI to exit.
		 */
		System.exit(0);
	}


	public void actionPerformed(ActionEvent ae) {

		if(ae.getSource() == allocateRefButton){
			/* user has pressed the allocate ref button. Therefore 
			 * we want to call the method that deals with allocating  
			 * referees and displaying them to the user. This will be 
			 * the allocateReferees() method.
			 */

			allocation();

		}
		else if(ae.getSource() == manageRefButton){
			/* user has pressed the manageRefButton. Therefore 
			 * we want to call the method that deals with editing  
			 * referee details. This will be the editReferees() method.
			 */

			manageRef();

		}
		else if(ae.getSource() == searchRefButton){
			/* user has pressed the search button. We will go to the method
			 * searchRef() which will call the method within refereeListing 
			 * that performs a linear search on the referee ID's. The matching
			 * info will be displayed to the user via a pop up window.
			 */
			searchRef();

		}
		else if(ae.getSource() == visualStatsButton){
			/* user has pressed the Visual Statistics button, so we need to access
			 * the displayChart() method which will access the bar chart class and 
			 * display the bar chart
			 */
			displayChart();

		}
		else if(ae.getSource() == saveExitButton){
			/* user has pressed the save and exit button. Here we need to save the 
			 * current list of referees and save them to a file named RefereesOut.txt
			 */
			saveAndExit();
		}
	}

}
