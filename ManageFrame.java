import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;

public class ManageFrame extends JFrame implements ActionListener{

	/* searchPanel is in the north area of the GUI, which holds the search functionality 
	 * for a referee. The centerPanel provides a gridlayout for the contents to be displayed.
	 * This will contain the panels holding home/qualification/travel/allocation information.
	 * The southPanel will hold the navigation buttons.
	 */
	private JPanel searchPanel, centerPanel, southPanel, buttonPanel, cancelPanel;
	private JPanel addDelEdPanel, contentPanel;
	private JPanel qualPanel, homePanel, allocPanel, travelPanel;
	private JPanel homePanelButton, homePanelLabel;
	private JPanel travelLabel, travelCheckBoxes;
	private JPanel qualPanelLabel, qualPanelMenu;
	private JFrame editRefDisplay;

	//private JButton addButton, deleteButton, edit,
	private JButton cancelButton, backButton;
	protected JButton submitAdd, submitDelete, submitEdit, searchName; 

	private JButton add, delete, edit;

	private JTextField refNameField, allocField;
	private JLabel refName, refQual, refHome, matchAlloc, refTravel;

	//homeOps - home locality... north, central, south
	private ButtonGroup homeOps;
	//private JRadioButton add, delete, edit;
	private JRadioButton homeNorth, homeCentral, homeSouth, invisible;	

	//travel avaliablity 
	private JCheckBox checkNorth, checkCentral, checkSouth;

	//list of qualifications
	private JComboBox<String> qualDropDown;

	private RefereeListing refereeListing;

	private boolean displayIsExpanded = false;

	//need an instance of JBallGUI to call updateDisplay(0)
	private final JBallGUI gui;

	private final int MAX_REFS = 12;

	// used to update the title on manage window
	private String manageAction = "Manage Referees ";


	public ManageFrame(RefereeListing refereeListing, JBallGUI _gui){

		this.refereeListing = refereeListing;

		gui = _gui;

		/* this method is used to edit a particular referee. To do this,
		 * this method displays another window to the user.
		 */
		editRefDisplay = new JFrame();

		//add a window listener to editRefDisplay so that we can set manage to null on the window closing
		editRefDisplay.addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent e)
			{
				//set manage to null in GUI
				gui.manageNull();
			}
		});

		// spec for the main display 
		editRefDisplay.setSize(600,330);
		editRefDisplay.setResizable(false);
		editRefDisplay.setTitle(manageAction);
		editRefDisplay.setVisible(true);
		editRefDisplay.setLocation(300,300);

		// instantiate JPanels
		addDelEdPanel = new JPanel(new BorderLayout());
		editRefDisplay.add(addDelEdPanel, BorderLayout.NORTH);

		contentPanel = new JPanel(new BorderLayout());
		editRefDisplay.add(contentPanel, BorderLayout.CENTER);
		contentPanel.setVisible(false);

		searchPanel = new JPanel();
		centerPanel = new JPanel(new GridLayout(2,2));
		southPanel = new JPanel();
		buttonPanel = new JPanel(new FlowLayout());
		cancelPanel = new JPanel(new FlowLayout());

		// instantiate JLabels
		refName = new JLabel("Name: ");
		refQual = new JLabel("Qualification: ");
		refHome = new JLabel("Home Locality: ");
		matchAlloc = new JLabel("Match Allocation: ");
		refTravel = new JLabel("Travel Willingness: ");

		// instantiate JButtons
		backButton = new JButton("Back");
		backButton.addActionListener(this);

		cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(this);

		searchName = new JButton("Search");
		searchName.addActionListener(this);

		submitAdd = new JButton("Submit");
		submitAdd.addActionListener(this);
		submitDelete = new JButton("Submit");
		submitDelete.addActionListener(this);
		submitEdit = new JButton("Submit");
		submitEdit.addActionListener(this);

		// instantiate JTextfields 
		refNameField = new JTextField(20);
		allocField = new JTextField(2);

		/* instantiate and add actionlisteners to the 
		 * add/delete/edit buttons. */

		add = new JButton("Add Referee");
		add.addActionListener(this);

		delete = new JButton("Delete Referee");
		delete.addActionListener(this);

		edit = new JButton("Edit Referee");
		edit.addActionListener(this);

		/* instantiate and add actionListeners to 
		 * to the home radio buttons */

		homeNorth = new JRadioButton("North");
		homeNorth.addActionListener(this);
		homeCentral = new JRadioButton("Central");
		homeCentral.addActionListener(this);
		homeSouth = new JRadioButton("South");
		homeSouth.addActionListener(this);
		invisible = new JRadioButton();
		invisible.addActionListener(this);	

		// button group for the home operations
		homeOps = new ButtonGroup();	

		/* the only thing we want in the north panel 
		 * is the buttons to choose add/delete/edit
		 * and also the cancel button
		 */
		addDelEdPanel.add(buttonPanel, BorderLayout.NORTH);
		addDelEdPanel.add(cancelPanel, BorderLayout.SOUTH);

		// add to the button panel 
		buttonPanel.add(add);
		buttonPanel.add(delete);
		buttonPanel.add(edit);

		// add to the cancel panel
		cancelPanel.add(cancelButton);

		// add JComponents to content panel
		addSearchPanel();
		addCenterPanel();
		addQualPanel();
		addHomePanel();
		addAllocPanel();
		addTravelPanel();
		addSouthPanel();
	}

	/* The search functionality is added to the 
	 * JPanel in this method.
	 */
	public void addSearchPanel()
	{
		// add seach JComponents to search panel 
		searchPanel.add(refName);
		searchPanel.add(refNameField);
		searchPanel.add(searchName);

		// add the search panel to the content panel 
		contentPanel.add(searchPanel, BorderLayout.NORTH);
	}

	/* need to create a JPanel for each grid position
	 * this is because we want to add more than one 
	 * component to each grid position.
	 */
	public void addCenterPanel()
	{
		/* instantiate the panels to 
		 * the center
		 */ 
		qualPanel = new JPanel();
		// make two new panels to be added to the qualPanel
		qualPanelLabel = new JPanel();
		qualPanelMenu = new JPanel();
		// component needed for the qualPanel
		qualDropDown = new JComboBox<String>();


		homePanel = new JPanel();
		// make two new panels to be added to the home panel
		homePanelLabel = new JPanel();
		homePanelButton = new JPanel();		

		allocPanel = new JPanel();

		travelPanel = new JPanel();
		// make two new panels to be added to the travel panel
		travelLabel = new JPanel();
		travelCheckBoxes = new JPanel();
		// check boxes to be added to the travelCheckBox panel
		checkNorth = new JCheckBox("North");
		checkCentral = new JCheckBox("Central");
		checkSouth = new JCheckBox("South");

		// Adding panels to center panel 
		centerPanel.add(qualPanel);
		centerPanel.add(homePanel);
		centerPanel.add(allocPanel);
		centerPanel.add(travelPanel);

		contentPanel.add(centerPanel, BorderLayout.CENTER);
	}

	public void addQualPanel()
	{
		// adding qualPanel components 
		qualPanelLabel.add(refQual);
		qualPanel.add(qualPanelLabel, BorderLayout.EAST);

		// adding items to the drop down menu
		qualPanelMenu.add(qualDropDown);
		qualDropDown.addItem("Select");
		qualDropDown.addItem("NJB1");
		qualDropDown.addItem("NJB2");
		qualDropDown.addItem("NJB3");
		qualDropDown.addItem("NJB4");
		qualDropDown.addItem("IJB1");
		qualDropDown.addItem("IJB2");
		qualDropDown.addItem("IJB3");
		qualDropDown.addItem("IJB4");

		// the drop down cannot be edited
		qualDropDown.setEditable(false);
		qualPanel.add(qualPanelMenu, BorderLayout.WEST);
	}

	public void addHomePanel()
	{
		//Home Panel components

		// radio buttons in the homeOps group
		homeOps.add(homeNorth);
		homeOps.add(homeCentral);
		homeOps.add(homeSouth);
		homeOps.add(invisible);
		/* invisible is only used when we want to 
		 * reset the fields and make it look like 
		 * there are no radio buttons active. The
		 * user will never see this button.
		 */
		invisible.setVisible(false);

		// position the home panel 
		homePanelLabel.add(refHome, BorderLayout.NORTH);	
		homePanelButton.add(homeNorth, BorderLayout.NORTH);
		homePanelButton.add(homeCentral, BorderLayout.CENTER);
		homePanelButton.add(homeSouth, BorderLayout.SOUTH);

		homePanel.add(homePanelLabel, BorderLayout.WEST);
		homePanel.add(homePanelButton, BorderLayout.EAST);

	}

	public void addAllocPanel()
	{
		// add components to allocPanel
		allocPanel.add(matchAlloc);
		allocPanel.add(allocField);

	}

	public void addTravelPanel()
	{
		// add travel components
		travelLabel.add(refTravel, BorderLayout.NORTH);
		travelCheckBoxes.add(checkNorth, BorderLayout.NORTH);
		travelCheckBoxes.add(checkCentral, BorderLayout.CENTER);
		travelCheckBoxes.add(checkSouth, BorderLayout.SOUTH);

		travelPanel.add(travelLabel, BorderLayout.EAST);
		travelPanel.add(travelCheckBoxes, BorderLayout.WEST);

	}

	public void addSouthPanel()
	{
		// add south panel components
		southPanel.add(backButton);
		southPanel.add(submitAdd);
		southPanel.add(submitDelete);
		southPanel.add(submitEdit);	

		// add the south panel to the content panel 
		contentPanel.add(southPanel,BorderLayout.SOUTH);

	}

	public void exitWindow(){
		/* we want to exit the window but not the program
		 * so we have set the visibility of the JFrame to 
		 * be false.
		 */
		editRefDisplay.setVisible(false);

		//set manage to null in GUI
		gui.manageNull();
	}

	/*
	 * Handles add ref functionality as well as validating all input.
	 */
	public void addRef(){

		// pull name from text field, call nameFormat() and store as string
		String name = gui.nameFormat(refNameField.getText().trim());

		nameValid(name);

		// alloc is declared here as it needs to be input validated
		int alloc =  0;

		// Catch any illegal input from Allocation Field before parsing to int
		try{
			alloc = Integer.parseInt(allocField.getText().trim());
		}
		catch(NumberFormatException e){
			// If field is empty or contains non-numerical characters, set alloc to -1
			// This means it will be automatically caught by the input validation below.
			alloc = -1;
		}

		/* check the information entered is valid */
		if(allocField.getText().isEmpty() || qualDropDown.getSelectedItem().equals("Select")){
			JOptionPane.showMessageDialog
			(null, "Fields cannot be empty.  Also, be sure to select a qualification.", "Warning", JOptionPane.ERROR_MESSAGE);
		}
		// check for invalid input
		// this works in conjunction with try/catch
		else if(alloc < 0){
			JOptionPane.showMessageDialog
			(null, "Match Allocation field contains invalid input", "Warning", JOptionPane.ERROR_MESSAGE);
			allocField.setText("");
		}
		// check for numerical characters in string
		else if(gui.checkNumericalInput(name)){
			// display message warning
			JOptionPane.showMessageDialog
			(null, "The referee's name cannot contain numbers", "Warning", JOptionPane.ERROR_MESSAGE);
		}
		// check list before adding to ensure max refs hasnt beed reached.
		else if(refereeListing.numRefs() == MAX_REFS){
			JOptionPane.showMessageDialog
			(null, "The number of Referees is currently at maximum(12). Please delete one to make room", "Warning", 
					JOptionPane.ERROR_MESSAGE);
		}
		// check if the name contains first and surnames
		else if (!nameValid(name))
		{
			JOptionPane.showMessageDialog
			(null, "Please provide a first name and surname,\nseparated by only one space.\nNames like John Smith are supported,\nwhile John von Neumann is not.", "Name input error", JOptionPane.ERROR_MESSAGE);

			resetFields();
		}
		// check if ref already exists
		else if(refereeListing.hasRef(name)){
			JOptionPane.showMessageDialog
			(null, "This referee is already in the list.", "Name must be unique", JOptionPane.ERROR_MESSAGE);

			resetFields();
		}
		else { // if everything passes, send textField values to RefereeListing

			// take 	
			String home = this.getHomeSelected();
			String travel = this.travelChoices();
			String qual = (String)qualDropDown.getSelectedItem();

			// Send referee to refereeListing to be created and added
			refereeListing.addRef(name, qual, alloc, home, travel);

			// show update on the gui
			gui.updateDisplay();

			// success message
			JOptionPane.showMessageDialog
			(null,  name + " has been added sucessfully!", "Valid Input", JOptionPane.PLAIN_MESSAGE);

			resetFields();

		}			
	}

	// returns true if name contains some characters followed by a space followed by some other characters
	public boolean nameValid(String name)
	{
		boolean validity = false;
		int spaceCount = 0;

		for(int i=0;i<name.length();i++){

			// get letter at position i in name
			char letter = name.charAt(i);

			//if letter is space
			if(letter == ' ')
			{
				spaceCount++;
			}
		}

		// if the length of the name is nonzero, and the name consists of some characters (hopefully a first name)
		// followed by a space, followed by some more characters (hopefully a second name)
		if (name.length() != 0 && name.matches("(.*) (.*)") && spaceCount == 1)
		{
			validity = true;
		}

		return validity;
	}

	/*
	 * Handles all delete ref functionality as well as input validation
	 */
	public void deleteRef(){

		// pull name from text field, call nameFormat() and store as string
		String name = gui.nameFormat(refNameField.getText().trim());
		/* check validation */

		if(name.isEmpty()){
			JOptionPane.showMessageDialog
			(null, "Field cannot be empty", "Warning", JOptionPane.ERROR_MESSAGE);
		}
		// check for numerical characters in string
		else if(gui.checkNumericalInput(name)){
			// display message warning
			JOptionPane.showMessageDialog
			(null, "This field cannot contain numbers", "Warning", JOptionPane.ERROR_MESSAGE);
		}
		else if(!refereeListing.hasRef(name)){

			JOptionPane.showMessageDialog
			(null, "No match found. Please amend your search.", "Warning", JOptionPane.ERROR_MESSAGE);

		}
		else{
			//send the name to RefereeListing
			refereeListing.deleteRef(name);	

			//show update on gui 
			gui.updateDisplay();

			//delete successful message
			JOptionPane.showMessageDialog
			(null,  name + " has been removed.", "Delete Successful", JOptionPane.INFORMATION_MESSAGE);

			resetFields();
		}
	}

	/* 
	 * Handles edit referee functionality
	 * This deals with limited input validation, remainder is handled by searchCheck()
	 * 
	 */
	public void editRef(){
		// get the info from the relevant textfields
		String name = refNameField.getText().trim();
		Referee ref = refereeListing.searchRef(name);

		/* check validation */
		if(name.isEmpty()){
			// Check for empty name field
			JOptionPane.showMessageDialog
			(null, "Field cannot be empty", "Warning", JOptionPane.ERROR_MESSAGE);
		}
		else if(qualDropDown.getSelectedItem().equals("Select")){
			// check that qualification dropdown option has been chosen
			JOptionPane.showMessageDialog
			(null, "Please select a qualification.", "Warning", JOptionPane.ERROR_MESSAGE);
		}
		else{
			// if validation passes, extract choices from gui and store are strings
			String qual = (String)qualDropDown.getSelectedItem();
			String travel = travelChoices();
			String home = getHomeSelected();


			// send over the edited info to ref Listing	
			refereeListing.editRef(name, qual, home, travel);

			// update on the display
			gui.updateDisplay();

			//success dialog
			JOptionPane.showMessageDialog(this, "Edit Saved", "Successful Edit", JOptionPane.INFORMATION_MESSAGE);
			searchOnly();
			resetFields();
		}
	}

	/* this method displays only the JComponents needed to allow a user to 
	 * add a referee. Errors will be shown if incorrect data is entered.
	 */
	public void addVisibility(){
		// update the title of the manage referees window
		editRefDisplay.setTitle(manageAction + "- Add Referee");

		/* we must set the following panels to true
		 *  in case the user calls delete, or edit before
		 *  add
		 */
		qualPanel.setVisible(true);
		allocPanel.setVisible(true);
		travelPanel.setVisible(true);
		homePanel.setVisible(true);
		searchPanel.setVisible(true);
		contentPanel.setVisible(true);

		/* Set the following buttons to be false as 
		 * these are not required when we are adding  
		 * a ref.
		 */
		searchName.setVisible(false);
		add.setVisible(false);
		delete.setVisible(false);
		edit.setVisible(false);
		cancelButton.setVisible(false);
		submitEdit.setVisible(false);
		submitDelete.setVisible(false);

		/* set the following buttons to true. 
		 * These are required.
		 */
		backButton.setVisible(true);
		submitAdd.setVisible(true);
		// if edit has been pressed before
		allocField.setEditable(true);

		// make the travel checkboxes editable
		checkNorth.setEnabled(true);
		checkCentral.setEnabled(true);
		checkSouth.setEnabled(true);


	}


	/* this method displays only the JComponents needed to allow a user to 
	 * delete a referee. Errors will be shown if incorrect data is entered.
	 */
	public void deleteVisibilty(){
		// update the title of the manage referees window
		editRefDisplay.setTitle(manageAction + "- Delete Referee");

		/* in delete, all the user needs to see is the 
		 * search field along with the delete/back buttons.
		 * Everything else should be set to false visibility.
		 */ 
		contentPanel.setVisible(true);

		// panels that shouldnt be seen 
		qualPanel.setVisible(false);
		allocPanel.setVisible(false);
		travelPanel.setVisible(false);
		homePanel.setVisible(false);
		searchPanel.setVisible(true);

		// do not need the following buttons for delete
		add.setVisible(false);
		edit.setVisible(false);
		delete.setVisible(false);
		cancelButton.setVisible(false);
		searchName.setVisible(false);
		submitEdit.setVisible(false);
		submitAdd.setVisible(false);

		// need the submit and back buttons to be visible
		submitDelete.setVisible(true);
		backButton.setVisible(true);
	}


	/* this method displays only the JComponents needed to allow a user to 
	 * search for a referee. Errors will be shown if incorrect data is entered.
	 */
	public void searchVisibility(){	
		// update the title of the manage referees window
		editRefDisplay.setTitle(manageAction + "- Edit Referee");

		/* want the search field/button and back buttons to
		 * be visible, however we do not want to be able
		 * to see the qual/alloc/travel/home choices yet.
		 * We also dont want to see the add,delete,submit 
		 * and cancel buttons. Hence these need to be false
		 * for now.
		 */

		// set what panels are visible 
		contentPanel.setVisible(true);
		allocPanel.setVisible(false);
		travelPanel.setVisible(false);
		qualPanel.setVisible(false);
		homePanel.setVisible(false);
		searchPanel.setVisible(true);

		// setting the buttons to false visibility
		add.setVisible(false);
		delete.setVisible(false);
		edit.setVisible(false);
		submitEdit.setVisible(false);
		submitDelete.setVisible(false);
		submitAdd.setVisible(false);
		cancelButton.setVisible(false);
		// incase the add or delete button has been pressed before edit
		searchName.setVisible(true);
		backButton.setVisible(true);

	}

	/* here we checking the validation on the name field.
	 * If the field is empty or the name already exists in
	 * the list then the user sees an error message. If all 
	 * validation is passed then the full visibility of the 
	 * edit screen is shown.
	 */	
	public void searchCheck(){	

		// Take name and store as string for validation
		String name = refNameField.getText().trim();

		if(name.isEmpty()){
			// check for empty name field
			JOptionPane.showMessageDialog
			(null, "Field cannot be empty", "Warning", JOptionPane.ERROR_MESSAGE);
		}
		// check for numerical characters
		else if(gui.checkNumericalInput(name)){
			// display message warning
			JOptionPane.showMessageDialog
			(null, "This field cannot contain numbers", "Warning", JOptionPane.ERROR_MESSAGE);
		}
		// Check if ref exists
		else if(!refereeListing.hasRef(name)){
			// the ref we want to edit exists
			JOptionPane.showMessageDialog(this, "No Referee with that name exists", 
					"Warning", JOptionPane.ERROR_MESSAGE);


			resetFields();

		}
		else {
			// else validation has passed, set full visibility for edit ref fields
			editFullVisibility();
			// set default details for ref that has been searched for
			setEditDetails();
		}
	}

	/* this method is to show only the search operation. This 
	 * will be used when the user has submitted an edit and 
	 * is shown the search name functionality only. 
	 */
	public void searchOnly(){
		// reset name field back to empty
		refNameField.setText("");

		allocPanel.setVisible(false);
		travelPanel.setVisible(false);
		qualPanel.setVisible(false);
		homePanel.setVisible(false);

		submitEdit.setVisible(false);

		// make the travel checkboxes editable
		checkNorth.setEnabled(true);
		checkCentral.setEnabled(true);
		checkSouth.setEnabled(true);

	}

	/* this method is used to set the current information 
	 * stored in the arrayList for the name entered. This 
	 * more user friendly as they can view the current 
	 * standings and adjust, rather than having to enter 
	 * everything again. They can only view the allocation 
	 * and name they cannot edit these particular fields.
	 */
	public void setEditDetails(){

		// Take specified ref and store name as string
		String name = refNameField.getText();
		// using name String, retrieve specified ref
		Referee ref = refereeListing.searchRef(name);

		// Set allocation
		// This must be displayed but cannot be edited
		allocField.setText(""+ref.getAlloc());

		// Set Home
		if(ref.getHome().equals("North")){
			homeNorth.setSelected(true);
			checkNorth.setEnabled(false);

			checkCentral.setEnabled(true);
			checkSouth.setEnabled(true);
		}
		else if(ref.getHome().equals("Central")){
			homeCentral.setSelected(true);
			checkCentral.setEnabled(false);

			checkNorth.setEnabled(true);
			checkSouth.setEnabled(true);
		}
		else if(ref.getHome().equals("South")){
			homeSouth.setSelected(true);
			checkSouth.setEnabled(false);

			checkNorth.setEnabled(true);
			checkCentral.setEnabled(true);
		}

		// Set Qualification
		qualDropDown.setSelectedItem(ref.getQualif());

		/* Set Travel
		 * dependant on what home button is selected, the 
		 * corresponding check box for travel is selected.
		 */
		if(ref.getTravel().substring(0,1).equals("Y")){
			checkNorth.setSelected(true);
		}
		if(ref.getTravel().substring(1,2).equals("Y")){
			checkCentral.setSelected(true);
		}
		if(ref.getTravel().substring(2).equals("Y")){
			checkSouth.setSelected(true);
		}

	}

	/* this method displays all the JComponents needed to allow a user to 
	 * edit a referee. Errors will be shown if incorrect data is entered.
	 */
	public void editFullVisibility()
	{
		// update the title of the manage referees window
		editRefDisplay.setTitle(manageAction + "- Edit Referee");

		/* now we want to make the whole edit screen 
		 * visible to the user to allow editing.
		 */

		/* these were false when we were in the 
		 * partial view for edit, so these need to be
		 * changed back to true.
		 */
		contentPanel.setVisible(true);
		allocPanel.setVisible(true);
		travelPanel.setVisible(true);
		qualPanel.setVisible(true);
		homePanel.setVisible(true);

		// shouldn't be able to edit name or match allocs 
		refNameField.setEditable(false);
		allocField.setEditable(false);

		// we only need the submitEdit button to be visible
		add.setVisible(false);
		delete.setVisible(false);
		edit.setVisible(false);

		cancelButton.setVisible(false);
		submitEdit.setVisible(true);
		submitAdd.setVisible(false);
		submitDelete.setVisible(false);

		// make the travel checkboxes editable
		checkNorth.setEnabled(true);
		checkCentral.setEnabled(true);
		checkSouth.setEnabled(true);

	}

	/* This method resets the frame back to only being 
	 * able to see the choice buttons, i.e
	 * add, delete, edit and cancel. Everything
	 * else should be invisible.
	 */
	public void resetFrame()
	{
		// update the title of the manage referees window
		editRefDisplay.setTitle(manageAction);


		//set the panel visibility
		contentPanel.setVisible(true);
		searchPanel.setVisible(false);
		qualPanel.setVisible(false);
		homePanel.setVisible(false);
		allocPanel.setVisible(false);
		travelPanel.setVisible(false);
		southPanel.setVisible(true);

		/* set the button visibility false
		 * for the buttons w dont want to see
		 */
		backButton.setVisible(false);
		submitAdd.setVisible(false);
		submitDelete.setVisible(false);
		submitEdit.setVisible(false);

		// and likewise true for the ones we want visible
		add.setVisible(true);
		delete.setVisible(true);
		edit.setVisible(true);
		cancelButton.setVisible(true);

		// anything entered that we dont want anymore discard
		resetFields();
	}

	/* this method deals which JComponents are 
	 * set and enabled when the north radio button 
	 * is selected.
	 */	
	public void homeTravelNorth(){
		/* when north is selected for home locality
		 * the north check box is checked, and then 
		 * the central and south boxes are unchecked.
		 */
		checkNorth.setSelected(true);
		checkCentral.setSelected(false);
		checkSouth.setSelected(false);

		// set the north check box to be uneditable 
		checkNorth.setEnabled(false);	
		/* set these to be editable again incase 
		 * central or south radio buttons are pressed 
		 * initially.
		 */					
		checkCentral.setEnabled(true);
		checkSouth.setEnabled(true);
	}

	/* this method deals which JComponents are 
	 * set and enabled when the central radio button 
	 * is selected.
	 */
	public void homeTravelCentral(){
		/* when central is selected for home locality
		 * the central check box is checked, and then 
		 * the north and south boxes are unchecked.
		 */

		checkNorth.setSelected(false);
		checkCentral.setSelected(true);
		checkSouth.setSelected(false);

		// set the central check box to be uneditable 
		checkCentral.setEnabled(false);	
		/* set these to be editable again incase 
		 * north or south radio buttons are pressed 
		 * initially.
		 */
		checkNorth.setEnabled(true);
		checkSouth.setEnabled(true);	
	}

	/* this method deals which JComponents are 
	 * set and enabled when the south radio button 
	 * is selected.
	 */
	public void homeTravelSouth(){
		/* when south is selected for home locality
		 * the south check box is checked, and then 
		 * the north and central boxes are unchecked.
		 */
		checkSouth.setSelected(true);
		checkNorth.setSelected(false);
		checkCentral.setSelected(false);

		// set the south check box to be uneditable 
		checkSouth.setEnabled(false);	
		/* set these to be editable again incase 
		 * north or central radio buttons are pressed 
		 * initially.
		 */
		checkNorth.setEnabled(true);
		checkCentral.setEnabled(true);	
	}

	/* This method checks which travel check 
	 * boxes are pressed and builds a string 
	 * dependant on this information. This
	 * will be accessed and sent to RefereeListing.java
	 */
	public String travelChoices(){
		String travel = "";
		if(checkNorth.isSelected()){
			travel += "Y";
		}
		else{
			travel += "N";
		}
		if(checkCentral.isSelected()){
			travel += "Y";
		}
		else{
			travel += "N";
		}

		if(checkSouth.isSelected()){
			travel += "Y";
		}
		else{
			travel += "N";
		}

		return travel;
	}

	/* This method checks which radio button has been
	 * selected and returns this information in
	 * the form of a string.
	 */
	public String getHomeSelected(){
		String home = "";
		if(homeNorth.isSelected()){
			home += "North";
		}
		else if(homeCentral.isSelected()){
			home += "Central";
		}
		else if(homeSouth.isSelected()){
			home += "South";
		}
		return home;
	}

	// reset the fields to default values
	public void resetFields(){
		refNameField.setText("");
		allocField.setText("");
		/* make sure name is editable again after
		 * exiting the edit view.
		 */
		refNameField.setEditable(true);

		/* Need to have a radio button that 
		 * is not shown, this is because we 
		 * need to have one button in the 
		 * button group selected after one
		 * has originally selected.
		 */
		invisible.setSelected(true);

		checkNorth.setSelected(false);
		checkCentral.setSelected(false);
		checkSouth.setSelected(false);

		checkNorth.setEnabled(true);
		checkCentral.setEnabled(true);
		checkSouth.setEnabled(true);

		qualDropDown.setSelectedItem("Select");
	}

	public void actionPerformed(ActionEvent ae) {
		if(ae.getSource() == add){
			/* user wants to add a ref, go to the visibilty 
			 * method for this.
			 */
			addVisibility();
		}
		else if(ae.getSource() == delete){
			/* user wants to delete a ref, go to the visibilty 
			 * method for this.
			 */
			deleteVisibilty();
		}
		else if(ae.getSource() == edit){
			/* user wants to edit a ref, go to the visibilty 
			 * method for this. This will only show the name 
			 * search, which will subsequently lead to the 
			 * edit feature of the program.
			 */
			searchVisibility();
		}
		else if(ae.getSource() == cancelButton){
			/* user wants to cancel - therefore we need to exit 
			 * the manage referees window 
			 */
			exitWindow();
		}
		else if(ae.getSource() == submitAdd){
			/* user wants to submit there addition */
			addRef();
		}
		else if(ae.getSource() == submitDelete){
			/* user wants to submit there deletion */

			deleteRef();
		}
		else if(ae.getSource() == submitEdit){
			/* user wants to submit there edit */

			editRef();
		}
		else if(ae.getSource() == backButton){
			/* user wants to cancel - therefore we need to exit 
			 * the manage referees window 
			 */
			resetFrame();
		}
		else if(ae.getSource() == searchName){
			/* user wants to search for a ref on the list */
			searchCheck();
		}
		/* a referee has to attend matches in their home 
		 * locality. These next three actions allow us to 
		 * select the corresponding check box in travel as 
		 * they have pressed in the home locality radio buttons
		 */
		else if(homeNorth.isSelected()){
			homeTravelNorth();
		}
		else if(homeCentral.isSelected()){
			homeTravelCentral();
		}
		else if(homeSouth.isSelected()){
			homeTravelSouth();
		}
		else if(ae.getSource() == travelCheckBoxes){
			/* the check boxes are the only other buttons 
			 * that could be pressed
			 */ 
			travelChoices();
		}
	}
}