package caixeiroviajante;

import java.awt.*;
import javax.swing.*;

/**
 * Monta o Layout
 */
public class SettingsGUI extends JPanel{

	JPanel buttonPane1;
	JPanel buttonPane2;
	
	JTextField noCitiesField;
	JTextField widthField;
	JTextField heightField;
	JTextField populationField;
	JTextField mutationPField;
	JTextField crossoverPField;
	JTextField tournamentField;
	JTextField survivorTournamentField;
	JTextField delayField;
	
	/**
	 * 
	 * @param title name of the window created
	 */
	public SettingsGUI(){
		// create the window & set size of it
		setPreferredSize(new Dimension(280,300));
	
		// create a GUI
		
		// (A) top panel will set the problem
		JPanel topPane = new JPanel();
		JPanel fieldPane = new JPanel();
		fieldPane.setLayout(new GridLayout(3,2));
		topPane.setLayout(new BorderLayout());

		// add imputfields to set problem
		//fieldPane.add(new JLabel("Número de Cidades:"));
		noCitiesField = new JTextField(3);
		noCitiesField.setText(String.valueOf(TSPComponent.DEFAULT_NO_CITIES));
		//fieldPane.add(noCitiesField);
		fieldPane.add(new JLabel("Width of drawing(pix):"));
		widthField = new JTextField(3);
		widthField.setText(String.valueOf(TSPComponent.DEFAULT_AREA_WIDTH));
		fieldPane.add(widthField);
		fieldPane.add(new JLabel("Height of drawing(pix):"));
		heightField = new JTextField(3);
		heightField.setText(String.valueOf(TSPComponent.DEFAULT_AREA_HEIGHT));
		fieldPane.add(heightField);
		
		buttonPane1 = new JPanel();
		topPane.add(fieldPane, BorderLayout.NORTH);
		topPane.add(buttonPane1, BorderLayout.CENTER);
		//add(fieldPane);
		//add(buttonPane1);
		
		// (B) a bottom panel with parameter settings
		JPanel bottomPane = new JPanel();
		JPanel fieldPane2 = new JPanel();
		fieldPane2.setLayout(new GridLayout(6,2));
		bottomPane.setLayout(new BorderLayout());
			
		fieldPane2.add(new JLabel("Population Size:"));
		populationField = new JTextField(3);
		populationField.setText(String.valueOf(TSPComponent.DEFAULT_POPULATION_SIZE));
		fieldPane2.add(populationField);
		fieldPane2.add(new JLabel("Mutation Prob(%):"));
		mutationPField = new JTextField(3);
		mutationPField.setText("20");
		fieldPane2.add(mutationPField);
		fieldPane2.add(new JLabel("Crossover Prob(%):"));
		crossoverPField = new JTextField(3);
		crossoverPField.setText("70");
		fieldPane2.add(crossoverPField);
		fieldPane2.add(new JLabel("Tournmt. Size Parents:"));
		tournamentField = new JTextField(3);
		tournamentField.setText("5");
		fieldPane2.add(tournamentField);
		/* test didn't look good
		JPanel p = new JPanel();
		p.setLayout(new GridLayout(2,1));
		p.add(new JLabel("Tournament Size"));
		p.add(new JLabel("Survivor Selection"));
		fieldPane2.add(p);*/
		fieldPane2.add(new JLabel("Tournmt. Size Survivors:"));
		survivorTournamentField = new JTextField(3);
		survivorTournamentField.setText("5");
		fieldPane2.add(survivorTournamentField);
		fieldPane2.add(new JLabel("Generational Delay(ms):"));
		delayField = new JTextField(3);
		delayField.setText("0");
		fieldPane2.add(delayField);
		
		buttonPane2 = new JPanel();
		bottomPane.add(fieldPane2, BorderLayout.NORTH);
		bottomPane.add(buttonPane2, BorderLayout.CENTER);
		
		// put everything together
		setLayout(new BorderLayout());
		add(topPane, BorderLayout.NORTH);
		add(bottomPane, BorderLayout.CENTER);
	}
	
	public JPanel getButtonPane1(){
		return buttonPane1;
	}
	public JPanel getButtonPane2(){
		return buttonPane2;
	}
	// user input checking could be done here
	public int getCities(){
		return Integer.parseInt( noCitiesField.getText() );
	}
	public int getAreaWidth(){
		return Integer.parseInt( widthField.getText() );
	}
	public int getAreaHeight(){
		return Integer.parseInt( heightField.getText() );
	}
	public int getPopulationSize(){
		return Integer.parseInt( populationField.getText() );
	}
	public int getMutationP(){
		return Integer.parseInt( mutationPField.getText() );
	}
	public int getCrossoverP(){
		return Integer.parseInt( crossoverPField.getText() );
	}
	public int getTournamentSize(){
		return Integer.parseInt( tournamentField.getText() );
	}
	public int getSurvivorTournamentSize(){
		return Integer.parseInt( survivorTournamentField.getText() );
	}
	public int getDelay(){
		return Integer.parseInt( delayField.getText() );
	}
}//end class
