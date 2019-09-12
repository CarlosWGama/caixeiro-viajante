package caixeiroviajante;

import algoritmogenetico.*;

import javax.swing.*;
import java.awt.*;
import java.util.*;

/**
 * A component that displays and rund a Genetic Algorithm 
 * solving the travelling salsepersons' problem. <br><br>
 * 
 * Lastest Modification: 15.01.2004
 * 
 * @author Hanna Sjolinder, NUIG student no 03103854
 */
public class TSPComponent 
	extends JComponent
	implements Observer {
		
		public static int DEFAULT_NO_CITIES = 30;
		public static int DEFAULT_AREA_WIDTH = 800;
		public static int DEFAULT_AREA_HEIGHT = 800;
		public static int DEFAULT_POPULATION_SIZE = 200;
		
		// number of cities in simulation
		private int numberOfCities=DEFAULT_NO_CITIES;
		
		// size of the area the cities are located in
		private int areaWidth=DEFAULT_AREA_WIDTH;
		private int areaHeight=DEFAULT_AREA_HEIGHT;
		
		// the algorithm that will solve the problem 
		private GeneticAlgorithm GA;
		
		// population size
		private int populationSize = DEFAULT_POPULATION_SIZE;
		
		// the fittest solution so far & latest painted generation 
		private TSPIndividual fittestIndividual;
		private int generation;
			
		// flag for painting TSP stuff
		private boolean paintTSP = false;	
		private boolean paintCities = false;
		private boolean GAinitialized = false;
			
	public boolean getGAinitialized(){
		return GAinitialized;
	}
	public void setDimension(int x,int y){
		areaWidth = x;
		areaHeight = y;
		setPreferredSize(new Dimension(x+20,y+20));
	}
	public void setPopulationSize(int n){
		populationSize = n;
	}
	public void setMutationProbability(int n){
		GA.setMutationProbability(n);
	}
	public void setCrossoverProbability(int n){
		GA.setRecombineProbability(n);
	}
	public void setTournamentSize(int n){
		GA.setParentTournamentSize(n);
	}
	public void setSurvivorTournamentSize(int n){
		GA.setSurvivorTournamentSize(n);
	}
	public void setDelay(int n){
		GA.setGenerationalDelay(n);
	}
	
	/**
	 * Lays out cities in the TSPIndividual and makes sure that
	 * the gene length matches this. That is this methods creates
	 * a new probem to solve
	 * 
	 * @param noCities # cities to create
	 * @param width max location for a city in x-dir.
	 * @param height max location for a city in y-dir.
	 */
	public void initializeCities(int noCities, int width, int height){		
		boolean random = false;
		if (random) {
			// create cities
			TSPIndividual.createRandomCities(	noCities, 
											width, 
											height );
			// make sure the gene length match the # cities
			PermutationGenotype.setGeneLength( noCities );
		} else {
			TSPIndividual.createCities(5, width, height );
			// make sure the gene length match the # cities
			PermutationGenotype.setGeneLength( 5 );
		}
		
		paintCities = true;
		GAinitialized = false;
		repaint();
	}

	/**
	 * Creates a Genetic Algorithm and sets this component as
	 * an observer of the same algorithm. Assumes that the
	 * cities have been initialized prior to call.
	 */
	public void initializeGA(){
		// 1) make sure no GA is active
		if (GA != null){
			stopGA();
			GA.deleteObserver(this);
		}
		
		// 2) create a Genetic Algorithm to solve our problem
		GA = new GeneticAlgorithm(	populationSize, new TSPIndividual() );
		
		// 3) register this TSPComponent as an observer of the GA
		//    and ensure that the first update is recieved before
		//    we try to paint the information from it.
		GA.addObserver(this); 
		GA.getFirstUpdate();
		
		GA.deleteObserver(this);
		
		// 4) flag
		GAinitialized = true;
	}

	/**
	 * Starts a tread running the Genetic algorithm. Assumes
	 * that the GA has been initialized prior to call.
	 */
	public void startGA(){	
		// start a thread to run the GA
		// run the GA (new thread need to be created when this dies...)
		GAThread algorithmThread = new GAThread();
		algorithmThread.start();
		
		// signal start painting the TSP stuff 
		paintTSP = true;
		paintCities = false;
	}
	
	/**
	 * Stops the genetic algorithms thread of execution. Note that the
	 * GA object is still left and can be restarted.
	 */
	public void stopGA(){
		// stop painting the TSP graph
		paintTSP = false;
		
		// close down
		synchronized (GA) {
			GA.close();
		}
	}

	/**
	 * paints the tsp component. Depending on states if shows 
	 * different things:<br><br>
	 * 
	 * (A) no GA created -> nothing is shown
	 * (B) cities initiealized -> cities drawn
	 * (C) GA started -> 
	 * 	   showing cities, path, gene and fitness of fittest individual
	 *     as well as the generation number
	 */
	public void paint(Graphics g){
		super.paint(g);
		
		if(paintCities){
			TSPIndividual.drawCities(g,0,0);
		}
		
		if(paintTSP){
			// the syncronization here ensures that all activity within the
			// GA is blocked until the current state is displayed to the user.
			// this gives that the running of the algorithm can never be faster
			// than its drawing since it always has to wait for this.
			synchronized (GA) {
				g.drawString("generation: " + generation, 2, 10);	
				fittestIndividual.drawIndividual(g,0,0,areaWidth,areaHeight);
				
				// only for debug, gives ConcurrentModificationException
				// drawAllIndividuals(g);
			}
		}
	}
	
	/**
	 * Recieves updates from the GA and sets the corresponding
	 * class variables so the paint methods can use the information.
	 * @param o the genetic algorithm we're observing
	 * @param info an object of type GeneticAlgorithmInfo
	 *        that is sent from the GA.
	 */
	public void update(Observable o, Object info){
		fittestIndividual = (TSPIndividual)((GeneticAlgorithmInfo)info).getFittestIndividual();
		generation = ((GeneticAlgorithmInfo)info).getGenerationNumber();
		repaint();
	}
	
	/**
	 * for debug use, not thread safe
	 *  
	 * @param g
	 */
	private void drawAllIndividuals(Graphics g){
		Iterator it = GA.iterator();
		int counter = 60;
		while(it.hasNext()){
			TSPIndividual individual = (TSPIndividual)it.next();
			individual.drawIndividual(g,40,counter,areaWidth,areaHeight);
			counter += areaHeight + 30;
		}
	}
	
	/**
	 * Thread representing a run of the GA
	 */
	private class GAThread extends Thread {
		public void run() {
			GA.run();
		}
	}
	
}//end class
