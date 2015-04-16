import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel; 
import org.jfree.chart.JFreeChart; 
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset; 
import org.jfree.data.category.DefaultCategoryDataset; 
import org.jfree.ui.ApplicationFrame; 
import java.awt.event.WindowEvent;

/**
 * This BarChart has been created using the JFreeChart API. 
 */

public class BarChart extends ApplicationFrame{
	private RefereeListing rListing;
	//need an instance of JBallGUI to call displayChartNull()
	private final JBallGUI gui;

	public BarChart(String appName , String cTitle, RefereeListing refs, JBallGUI _gui)
	{
		super(appName);     
		rListing = refs;
		gui = _gui;
		JFreeChart barChart = ChartFactory.createBarChart(
				cTitle,           //window title
				"Referee",       //y axis title     
				"Matches Refereed", //x axis title            
				createDataset(), //creating the dataset to be used the in the graph         
				PlotOrientation.VERTICAL,    //Y axis is vertical       
				false, //this boolean sets the legend to invisible
				false, //this boolean makes the tool tips invisible 
				false); //this boolean set as false means urls will not be generated


		ChartPanel cPanel = new ChartPanel(barChart);   //chart added to a panel    
		cPanel.setPreferredSize(new java.awt.Dimension(760,460));        
		setContentPane(cPanel);

	}
	/**
	 * Creating the dataset for the chart.
	 * @return dataset for the chart
	 */

	private CategoryDataset createDataset( )
	{
		final DefaultCategoryDataset dataset = new DefaultCategoryDataset(); 

		for (int i = 0; i < rListing.numRefs(); i++){ //keep looping for as long as there are refs in the system

			//x axis category should be the ref ID, which come already alphabetically sorted from Ref Listing
			String xAxisElements = rListing.refAtIndex(i).getRefID();
			String rowKey = "rowKey" ; //same rowKey means all the bars are the same colour
			int yAxisValues = rListing.refAtIndex(i).getAlloc(); //y axis values in int form

			dataset.addValue(yAxisValues, rowKey, xAxisElements); //format int, string, string
		}

		return dataset;      
	}

	/**
	 * Overrides the ApplicationFrame from closing and only closes the BarChart Window
	 */
	public void windowClosing(final WindowEvent event) {
		if (event.getWindow() == this) {
			dispose();
			gui.displayChartNull();
		}
	}

}

