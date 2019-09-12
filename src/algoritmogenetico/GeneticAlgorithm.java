package algoritmogenetico;

import java.util.*;

/**
 * A basic class for running genetic algoritms.<br><br>
 * 
 * Lastest Modification: 15.01.2004
 * 
 * @author Hanna Sjolinder, NUIG student no 03103854
 */
public class GeneticAlgorithm  extends Observable{
	
	private Collection population;
	private Collection parents;
	private Collection offspring;
	
	private int generationCounter;
	private boolean stop;
	private int delay;
	
	private Individual fittestIndividual;
	
	// algorithm parameters
	/**
	 * probability of mutation of a any individual in percent 
	 */
	private int mutationProbability;
	private int mutationType;
	/**
	 * probability of recombination in percent. Will result
	 * in that on average recombineProbability % of the 
	 * population will be given the chance to reproduce  
	 */
	private int recombineProbability;
	private int recombineType;
	/**
	 * no of individuals in population
	 */
	protected int populationSize;
	
	protected int parentTournamentSize;
	protected int survivorTournamentSize;
	
	private boolean debug=false;
	
	/**
	 * Constructor that creates an initial population of populationSize
	 * individuals of type individualType
	 * 
	 * @param populationSize # individuals in population
	 * @param individualType
	 */
	public GeneticAlgorithm(int populationSize, Individual individualType ){	
		// set populationsize
		this.populationSize = populationSize;
		
		// set this to be first generation
		generationCounter = 0;
		
		// we here set that the implementation of the population
		// collections are Vector:s. Could possibly be changed to
		// for speed-ups. Note that the matingPool and thereby the
		// offspring are assumed to be of the same size as the
		// "base" population
		population = new Vector(populationSize);
		parents = new Vector(populationSize);
		offspring = new Vector(populationSize);
		
		// initialize the population
		initializePopulation( individualType );
		
		// evaluate fitness for all individuals and find the
		// fittest individual so far
		calculateFitness( population );
		fittestIndividual = individualType.newIndividual();
		fittestIndividual = getFittestIndividual(population);
		
		// allow the algorithm to start
		stop = false;
	}
	
	public void setMutationProbability(int probability){
		mutationProbability = probability;
	}
	public void setMutationType(int type){
		mutationType = type;
	}
	public void setRecombineProbability(int probability){
		recombineProbability = probability;
	}
	public void setRecombineType(int type){
		recombineType = type;
	}
	public void setGenerationalDelay(int delay){
		this.delay = delay;
	}
	public void setParentTournamentSize(int k){
		parentTournamentSize = k;
	}
	public void setSurvivorTournamentSize(int k){
		survivorTournamentSize = k;
	}
	
	
	/**
	 * Creates populationSize # random individuals and adds
	 * them to the population
	 * @param individualType
	 */
	public void initializePopulation(Individual individualType){

		// create populationSize # individuals & add them
		for( int i=0; i<populationSize; i++){
			Individual individual = individualType.newIndividual();
			individual.randomize();
			population.add(individual);
		}
	}
	
	/**
	 * Calculates the fitness for att the individuals given.
	 * @param individuals to calculate fitness for
	 */
	private void calculateFitness( Collection individuals ){
		// get iterator for the collection
		Iterator it = individuals.iterator();
		Individual individual;
		
		// calculate fitness for the rest of the individuals
		while(it.hasNext()){
			individual = (Individual)it.next();
			individual.calculateFitness();
		}
	}
	
	/**
	 * Returns the fittsest member of a collection. Assumes 
	 * that the collection given contains at least one individual.
	 * If two solutions are equally fit the first one found will be 
	 * returned
	 * 
	 * @param individual find fittest among these
	 * @return the fittest individual in the collection
	 */
	private Individual getFittestIndividual( Collection individuals ){
		Iterator it = individuals.iterator();
		
		// set first individual to be the fittest
		Individual individual = (Individual)it.next();
		Individual fittest = individual;
		
		// calculate fitness for the rest of the individuals
		while(it.hasNext()){
			individual = (Individual)it.next();
			if(individual.fitterThan(fittest)){
				fittest = individual;
			}
		}
		return fittest;
	}
	
	/**
	 * Iterates through a population and mutates
	 * the individuals with a specified probability
	 * @param individuals
	 */
	private void mutate( Collection individuals ){
		// go through the population and mutate with
		// the specified probability
		Iterator it=individuals.iterator();
		while(it.hasNext()){
			Individual individual = (Individual)it.next();
			// draw a random number [0,100]
			// if the number < set probability mutate the individual
			if( (int) ( Math.random() * 100 ) <= mutationProbability){
				individual.mutate(mutationType);
			}
		}
	}
	
	/**
	 * Fills the parent collection ie creates a mating pool from the
	 * given individuals. Parent selection is done by tournament.
	 * 
	 * @param individuals collection to select parents from
	 * @param noParents #individual to place in the mating pool
	 */
	private void selectParents( Collection individuals, int noParents ){
		
		int addedParents = 0;
		
		while(addedParents < noParents){
			// pick tournamentsize individuals randomly
			// to a tournament
			if(debug)
				System.out.println("TOURNAMENT START with " + parentTournamentSize + " participants:");
			Collection tournament = new Vector();
		
			for( int i=0; i<parentTournamentSize; i++){
				Individual randomIndividual = randomPick(individuals);
				tournament.add(randomIndividual);
				if(debug)
					System.out.println(randomIndividual.toString());
			}
		
			Individual fittest = getFittestIndividual( tournament );
			parents.add(fittest);
			if(debug)
				System.out.println("WINNER WAS: " + fittest.toString() );
			addedParents++;
		}
/*	
		// debug just use all individuals as parents
		Iterator it=individuals.iterator();
		while(it.hasNext()){
			Individual individual = (Individual)it.next();
			parents.add(individual);			
		}
*/
	} //end selectParents()
	
	/**
	 * Fills the offspring population with individuals created
	 * from the parents population. Every pair of parents has
	 * a recombineProbability% chance to be recombinated. If the
	 * mating pool is odd one parent will only result in itself
	 * as offspring.
	 * @param matingPool the parents
	 */
	private void recombine( Collection matingPool ){		
		Iterator it=matingPool.iterator();
		while(it.hasNext()){
			Individual firstParent = (Individual)it.next();
			if(!it.hasNext()){
				// odd # parents, this individual is just
				// transfered to the offspring
				offspring.add(firstParent);
			}else{
				Individual secondParent = (Individual)it.next();
				
				// draw a random number [0,100]
				// if the number < set probability recombine the individuals
				// othervise just copy them to the offspring
				if( (int) ( Math.random() * 100 ) <= recombineProbability){
					Vector children = 
						firstParent.breed(	recombineType,
											secondParent);															
					offspring.add(children.elementAt(0));
					offspring.add(children.elementAt(1));					
				}else{
					offspring.add(firstParent);
					offspring.add(secondParent);
				}			
			}			
		}
/*			
		// debug just transfer all parents to become offspring
		Iterator it=matingPool.iterator();
		while(it.hasNext()){
			offspring.add( it.next() );
		}
*/
	} //end recombine
	
	/**
	 * Tandomly picks and individual in the given collection<br><br>
	 * note that this method uses the fact that the given collection 
	 * actually is a Vector. if implementation of population change the
	 * random pick has to be changed too.
	 * 
	 * @param individuals collection to pick from
	 */
	private Individual randomPick( Collection individuals ){
		int size = individuals.size();
		int randomIndex  = (int) ( Math.random() * size );
		Individual randomIndividual = (Individual)(((Vector)individuals).elementAt(randomIndex));
		return randomIndividual;
	}
	
	/**
	 * Selects survivors ie fills the population collection bases on
	 * the parent and offspring. Performs a tournament selection so that
	 * lower fit individuals also has a chance to survive. (though not the 
	 * survivorTournamentSize -1 worst individuals)
	 * @param offspring 
	 * @param parents
	 */
	private void selectSurvivors( Collection offspring, Collection parents ){
		
		population.clear();
		int addedSurvivors = 0;
		// "ugly hack" note: offspring now contains parents too 
		offspring.addAll(parents);
		
		while(addedSurvivors < populationSize){
			// pick tournamentsize individuals randomly
			// to a tournament
			if(debug)
				System.out.println("TOURNAMENT START with " + parentTournamentSize + " participants:");
			Collection tournament = new Vector();
		
			for( int i=0; i<survivorTournamentSize; i++){
				Individual randomIndividual = randomPick(offspring);
				tournament.add(randomIndividual);
				if(debug)
					System.out.println(randomIndividual.toString());
			}
		
			Individual fittest = getFittestIndividual( tournament );
			population.add(fittest);
			if(debug)
				System.out.println("WINNER WAS: " + fittest.toString() );
			addedSurvivors++;
		}
/*
 //debug: let all offspring survive and all parents die		
		population.clear();
		Iterator it=offspring.iterator();
		while(it.hasNext()){
			Individual individual = (Individual)it.next();
			population.add(individual);			
		}
*/
	} //end selectSurvivors()
	
	/**
	 * a call to this method indicates one generation step
	 * all selection, mutation & recombination is called from this
	 * method ie it is the "raw algorithm"
	 */
	private void epoch(){
		// indicate generation progress
		generationCounter++;
				
		// parent selection based on population ie the parents
		// collection will be filled
		selectParents( population, populationSize );
		
		// recombine parents ie the offspring collection will be filled
		recombine( parents );
		
		// mutate the offspring     
		mutate( offspring );
		
		// evaluate fitness for the off-spring & get the fittest
		// individual
		//fittestIndividual = calculateFitness( population );
		calculateFitness( offspring );
		
		// select survivors ie replace the "base" population
		selectSurvivors( offspring, parents );
		
		// find the fittest
		fittestIndividual = getFittestIndividual(population);
		
		// clear parents and offspring populations
		offspring.clear();
		parents.clear(); 
	}
	
	// debug, for getting all solutions
	// not thread safe
	public Iterator iterator(){
		return population.iterator();
	}
	
	/**
	 * Notifies the observers about the current state of the GA.
	 * Could be used eg for getting the initial state of a GA before
	 * you start running it.
	 */
	public void getFirstUpdate(){
		setChanged();
		notifyObservers(new GeneticAlgorithmInfo(fittestIndividual,generationCounter));
	}
	
	// run methods
	/**
	 * make the algorithm execution stop & algorithm
	 * object will die
	 */
	public void close(){
		stop=true;
	}
		
	/**
	 * Run the algorithm. Will run until something calls
	 * close(). 
	 */
	public void run(){
		while(!stop){
			// take one generation step
			epoch();
			
			// notify your observers of the change
			setChanged();
			notifyObservers(
				new GeneticAlgorithmInfo(	fittestIndividual,
											generationCounter ) );	

			// sleep as prescribed
			try{
				Thread.sleep(delay);	
			}catch(InterruptedException ie){
				//do nothing
			}				
		}	
		
		// make a restart possible
		stop = false;
	}
	
}//end class
