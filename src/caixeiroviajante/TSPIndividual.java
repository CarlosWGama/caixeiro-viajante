package caixeiroviajante;


import algoritmogenetico.*;

import java.awt.*;
import java.util.*;

/**
 * An object of this class represent an individual solution 
 * to the travelling salesman problem.<br><br>
 * 
 * the phonotype/interpretation is specified at class level<br>
 * -> you can only work with one problem at a time
 *    ie all your individuals at a certain time will 
 *    be a solution to one specific city layout.<br><br>
 * 
 * Lastest Modification: 15.01.2004
 * 
 * @author Hanna Sjolinder, NUIG student no 03103854
 */
public class TSPIndividual extends Individual{
	
	private static int noCities;
	private static int[][] cityArray;
	
	private static int larguraTela;
	private static int alturaTela;
	
	// constructors
	/**
	 * create an individual with gene.
	 * fitness is undecided
	 */
	public TSPIndividual(Genotype gene){
		this.gene = gene;
	}
	public TSPIndividual(){
		gene = new PermutationGenotype();
	}
	/**
	 * "call-back" constructor
	 */
	public Individual newIndividual(){
		return new TSPIndividual();
	}

	/**
	 * Creates a number of cities randomly scattered within the area
	 * maxWidth*maxHeight. This sets the phenotype for TSPIndividuals.
	 * 
	 * @param noCitiesToCreate
	 * @param maxWidth highest value of the locations' x component
	 * @param maxHeight highest value of the locations' y component
	 */
	public static void createRandomCities(	int noCitiesToCreate,
											int maxWidth,
											int maxHeight){
		// set # cities
		noCities = noCitiesToCreate;
		
		// randomly place the cities
		cityArray = new int[noCities][2];
		for(int i=0; i<noCities; i++){
			cityArray[i][0] = (int) ( Math.random() * maxWidth );	
			cityArray[i][1] = (int) ( Math.random() * maxHeight );	
		}
	}
	
	public static void createCities(int noCitiesToCreate, int maxWidth, int maxHeight){
			// seta o número de cidades
			noCities = noCitiesToCreate;
			
			// Cria as cidades com as coordenadas definidas
			cityArray = new int[noCities][2];
					
			//Cidade 1
			cityArray[0][0] =  565 ;//X	
			cityArray[0][1] =  575;//Y
			//Cidade 2
			cityArray[1][0] =  25 ;//X	
			cityArray[1][1] =  185 ;//Y
			//Cidade 3
			cityArray[2][0] =  345 ;//X	
			cityArray[2][1] =  750 ;//Y
			//Cidade 4
			cityArray[3][0] =  945;//X	
			cityArray[3][1] =  685 ;//Y
			//Cidade 5
			cityArray[4][0] =  845 ;//X	
			cityArray[4][1] =  655;//Y
	
			larguraTela = maxWidth;
			alturaTela = maxHeight;
			
//			for (int i = 0; i < noCities; i++) {
//				cityArray[i][0] = (int) ((cityArray[i][0] * maxWidth)/2000);
//				cityArray[i][1] = (int) ((cityArray[i][1] * maxHeight)/2000);		
//				System.out.println(cityArray[i][0] + "-" + cityArray[i][1]);
//			}
	}

   /**
 	* Calculate the length of the path for traversing the cities
 	* according to the gene and let this pathlength be the fitness
 	* of the individual
 	*/
	public void calculateFitness(){
		// path length starts at 0
		fitness=0;
		
		// get an iterator for the gene, returning index for one
		// city at a time	
		gene = getGene();
		Iterator geneIterator = ((PermutationGenotype)gene).iterator();
		
		// start at the fist city
		int firstCityIndex = ((Integer)geneIterator.next()).intValue();
		int prevCityIndex = firstCityIndex;
		int currentCityIndex = -1;
		
		// for every loop as long as there is something left of the gene:
		// * get the index to a city
		// * add the distance between this city and the last visited
		//   to the overall fitness/pathlength
		// * set this city to be the previous
		while(geneIterator.hasNext()){
			currentCityIndex = ((Integer)geneIterator.next()).intValue();
			
			Point previousCity = 
				new Point (	cityArray[prevCityIndex][0],
							cityArray[prevCityIndex][1]);
							
			Point currentCity = 
				new Point (	cityArray[currentCityIndex][0],
							cityArray[currentCityIndex][1]);
							
			fitness += previousCity.distance(currentCity);
			
			// debug
			// System.out.println( "" + previousCity + currentCity + previousCity.distance(currentCity));
		  	
		  	// step forward
		  	prevCityIndex=currentCityIndex;
		}
		
		// Finish by adding the distance back to the first city
		// from the last one pulled of the gene.
		Point firstCity = 
			new Point (	cityArray[firstCityIndex][0],
						cityArray[firstCityIndex][1]);
		Point currentCity = 
			new Point (	cityArray[currentCityIndex][0],
						cityArray[currentCityIndex][1]);		
		fitness += currentCity.distance(firstCity);
		
		// debug
		// System.out.println( "" + firstCity + currentCity + currentCity.distance(firstCity) + "\n");
		
	}
	
	/**
	 * A TSP-Solution is better than another if it has a lower fitness
	 */
	public boolean fitterThan(Individual compeditor){
		if( fitness < compeditor.getFitness() ){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * draws cities connected by lines to represent a solution to
	 * the TSP problem
	 * 
	 * @param g
	 */
	public void drawIndividual(Graphics g, int offsetX, int offsetY, int width, int height){
		gene = getGene();
		Iterator geneIterator = ((PermutationGenotype)gene).iterator();
		
		int first = ((Integer)geneIterator.next()).intValue();
		int previous = first;
		
		while(geneIterator.hasNext()){
			// get current city
			int current = ((Integer)geneIterator.next()).intValue();
			
			// draw the city salesperson is leaving						
			drawCity(	g, 
						cityArray[previous][0]+offsetX, 
						cityArray[previous][1]+offsetY, 
						previous );	

						
			//draw his trace
			g.drawLine(	getPosition(cityArray[previous][0])+offsetX,
						getPosition(cityArray[previous][1],true)+offsetY,
						getPosition(cityArray[current][0])+offsetX,
						getPosition(cityArray[current][1],true)+offsetY);
						
			previous = current;
		}
		
		// draw the last city					
		drawCity(	g, 
					cityArray[previous][0]+offsetX, 
					cityArray[previous][1]+offsetY, 
					previous);	
   
		// and the way back to the first city
		g.drawLine(	getPosition(cityArray[previous][0])+offsetX,
					getPosition(cityArray[previous][1], true)+offsetY,
					getPosition(cityArray[first][0])+offsetX,
					getPosition(cityArray[first][1], true)+offsetY);
					
		// draw gene and fitness information
		g.drawString("gene: " + gene.toString(), offsetX, 25);
		g.drawString("fitness: " + fitness, offsetX, 40);	
	}
	
	/**
	 * Draws a city
	 * 
	 * @param g 
	 * @param x x-coordinate of city
	 * @param y y-coordinate of city
	 * @param citynumber id of city
	 */
	public void drawCity(Graphics g, int x, int y, int citynumber){
		
		// circle for the city
		g.drawOval( getPosition(x)-2, getPosition(y, true)-2, 4, 4);
						
		// draw the number by this city
		//g.drawString( citynumber+"", getPosition(x)+3, getPosition(y, true));
		g.drawString( citynumber+"(" + x + "," + y + ")", getPosition(x)+3, getPosition(y, true));
	}
	
	/**
	 * draws all cities
	 * 
	 * @param g
	 * @param offsetX
	 * @param offsetY
	 */
	static public void drawCities(Graphics g, int offsetX, int offsetY){
		TSPIndividual individual = new TSPIndividual();
		for(int i=0; i<noCities; i++){
			individual.drawCity(g, 
								cityArray[i][0]+offsetX, 
								cityArray[i][1]+offsetY, 
								i);	
		}
	}
	
	/**
	 * for debug/test
	 */
	public static void main(String[] args) {
		// test the fitness calculation:
		// create 4 cities in a Square
		noCities=4;
		cityArray = new int[noCities][2];

		cityArray[0][0] = 0;
		cityArray[0][1] = 0;
		cityArray[1][0] = 2;
		cityArray[1][1] = 0;
		cityArray[2][0] = 2;
		cityArray[2][1] = 1;
		cityArray[3][0] = 0;
		cityArray[3][1] = 1;

		PermutationGenotype.setGeneLength(4);
		
		TSPIndividual individual = new TSPIndividual();
		individual.randomize();
		System.out.println(individual.getGene().toString());
		individual.calculateFitness();
		System.out.println("fitness:"+individual.getFitness());
		
	}
	
	public static int getPosition(int position) {
		return getPosition(position, false);

	}
	
	public static  int getPosition(int position, boolean altura) {
		int limite = (altura ? alturaTela : larguraTela); 
		return (int) ((position * limite)/2000);

	}
}//end class
