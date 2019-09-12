package caixeiroviajante;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * An application sporting the TSPComponent. It contains an object
 * of the class SettingsGUI, a panel with controls for the TSPComponenet,
 * to interact with the GA.<br><br>
 * 
 * Lastest Modification: 15.01.2004
 * 
 * @author Hanna Sjolinder, NUIG student no 03103854
 */
public class Main extends JFrame{
	
	// the component responible for running and displaying
	// the progress of the TSP
	TSPComponent TSP;
	
	boolean citiesInitialized = false;
	
	JButton cityButton;
	JButton runButton;
	JButton stopButton;
	JButton restartButton;
	
	public Main(){
		super("TSP solved by a Genetic Algorithm");		
	
		// (A) create the settings GUI and add to the application
		final SettingsGUI settingGUI = new SettingsGUI();
		final Container c = getContentPane();
		c.setLayout(new BorderLayout());	
		c.add(settingGUI, BorderLayout.WEST);
		
		// (B) create the TSP component and add.	
		TSP = new TSPComponent();
		c.add(TSP, BorderLayout.CENTER);
		
		// (C) create buttons and set actions for the same
		// first buttonpanel contains the "create cities button"
		JPanel buttonPane1 = settingGUI.getButtonPane1();	
		cityButton = new JButton("CREATE CITIES");
		cityButton.addActionListener(
			new ActionListener(){
				public void actionPerformed( ActionEvent e ){
					int noCities = settingGUI.getCities();
					int width = settingGUI.getAreaWidth();
					int height = settingGUI.getAreaHeight();
					TSP.setDimension(width,height);
					TSP.initializeCities(noCities, width, height);
					citiesInitialized = true;
					
					// button status
					runButton.setEnabled(true);	
					cityButton.setEnabled(true);
					stopButton.setEnabled(false);
					restartButton.setEnabled(false);
				}
			} //end ActionListener class
		); //end addActionListener
		buttonPane1.add(cityButton);
		
		// second buttonpanel contains the start and run buttons 
		JPanel buttonPane = settingGUI.getButtonPane2();
		runButton = new JButton("START/CONTINUE");
		runButton.addActionListener(
			new ActionListener(){
				public void actionPerformed( ActionEvent e ){
					// initialize cities if not done
					if(!citiesInitialized){
						int noCities = settingGUI.getCities();
						int width = settingGUI.getAreaWidth();
						int height = settingGUI.getAreaHeight();
						TSP.setDimension(width,height);
						TSP.initializeCities(noCities, width, height);
					}
					
					// set the populationsize and create the GA
					if( !TSP.getGAinitialized() ){
						TSP.setPopulationSize(settingGUI.getPopulationSize());
						TSP.initializeGA();
					}				

					// set the parameters of the GA
					TSP.setMutationProbability(settingGUI.getMutationP());
					TSP.setCrossoverProbability(settingGUI.getCrossoverP());
					TSP.setTournamentSize(settingGUI.getTournamentSize());
					TSP.setSurvivorTournamentSize(settingGUI.getSurvivorTournamentSize());
					TSP.setDelay(settingGUI.getDelay());
			
					// button status
					stopButton.setEnabled(true);
					runButton.setEnabled(false);
					restartButton.setEnabled(false);
					cityButton.setEnabled(false);
					
					// start the algorithm
					TSP.startGA();
				}
			} //end ActionListener class
		); //end addActionListener
		buttonPane.add(runButton);
		
		stopButton = new JButton("STOP/PAUSE");
		stopButton.addActionListener(
			new ActionListener(){
				public void actionPerformed( ActionEvent e ){
					// stop the algorithm
					TSP.stopGA();
					
					// button status
					stopButton.setEnabled(false);
					runButton.setEnabled(true);	
					restartButton.setEnabled(true);
					cityButton.setEnabled(true);
				}
			} //end ActionListener class
		); //end addActionListener
		buttonPane.add(stopButton);
		
		restartButton = new JButton("NEW RUN, SAME CITIES");
		restartButton.addActionListener(
			new ActionListener(){
				public void actionPerformed( ActionEvent e ){					
					// initialize a new Algorithm
					TSP.setPopulationSize(settingGUI.getPopulationSize());
					TSP.initializeGA();				

					// set the parameters of the GA
					TSP.setMutationProbability(settingGUI.getMutationP());
					TSP.setCrossoverProbability(settingGUI.getCrossoverP());
					TSP.setTournamentSize(settingGUI.getTournamentSize());
					TSP.setSurvivorTournamentSize(settingGUI.getSurvivorTournamentSize());
					TSP.setDelay(settingGUI.getDelay());
			
					// button status
					stopButton.setEnabled(true);
					runButton.setEnabled(false);
					restartButton.setEnabled(false);
					cityButton.setEnabled(false);
					
					// start the algorithm
					TSP.startGA();
				}
			} //end ActionListener class
		); //end addActionListener
		buttonPane.add(restartButton);
		
		// at the start, create cities is the only accessible button
		cityButton.setEnabled(true);
		runButton.setEnabled(false);
		stopButton.setEnabled(false);
		restartButton.setEnabled(false);
		
		// set window size and show
		setSize( 1080, 720 );
		setVisible(true);
	}

	public static void main(String[] args) {
		Main app = new Main();
		app.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
	}
	
}//end class
