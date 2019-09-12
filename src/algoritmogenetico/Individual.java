package algoritmogenetico;


import java.util.*;

/**
 * Represents an individual solution in a genetic algorithm.
 * An abstract class that should be inherited to be able to
 * represent a specific problems solution.<br><br>
 * 
 * To not make this to complicated fitness is of type double 
 * (could be restricting) and recombine is limited to two parents<br><br>
 * 
 * Lastest Modification: 25.11.2003
 * 
 * @author Hanna Sjolinder, NUIG student no 03103854
 */
public abstract class Individual {
	protected Genotype gene;
	protected double fitness;
	
	// constructors
	public Individual(){
	}
	public Individual(Genotype gene){
		this.gene = gene;
	}
	
	// accessors
	public Genotype getGene(){
		return gene;
	}
	public double getFitness(){
		return fitness;
	}
	public void setGene(Genotype gene){
		this.gene = gene;
	}
	
	// Class methods
	/**
	 * mutate this individual ie gene of this individual
	 * @param mutationType specifies type of mutation
	 */
	public void mutate(int mutationType){
		gene.mutate(mutationType);	
	}
	/**
	 * A cross-over of two individuals
	 * @param recombineType specifies type of recombination
	 * @param otherParent the individual to mate with
	 * @return the result of the cross-over
	 */
	public Vector breed(int recombineType, Individual otherParent){
		Vector childGenes = 
			gene.recombine(	recombineType, 
							otherParent.getGene());
		// create idividuals from the genes
		Individual child1 = newIndividual();
		child1.setGene((Genotype)childGenes.elementAt(0));
		Individual child2 = newIndividual();
		child2.setGene((Genotype)childGenes.elementAt(1));

		Vector children = new Vector(2);
		children.add(child1);
		children.add(child2);
		return children;
	}
	
	/**
	 * "Constructor in Disguise"<br><br>
	 * 
	 * This method should in a Individual-implementation 
	 * contain a call to a constructor taking a genotype as 
	 * argument. This method exist to be able to create 
	 * individuals in methods of the abstract Individual class.
	 * (in the crossover() and randomize() methods)<br><br> 
	 * 
	 * @return a newly created Individual
	 */
	public abstract Individual newIndividual();

	/**
	 * randomizes this individual
	 */
	public void randomize(){
		gene.randomize();
	}
	
	public String toString(){
		return getClass().getName() + " FITNESS: " + fitness + " GENE: " +gene.toString();
	}
	
	/**
	 * ensure that there for every individual there is a way to
	 * calculate its fitness 
	 */
	public abstract void calculateFitness(); 
	/**
	 * compares two individuals
	 * if the caller is better than the compeditor true will be
	 * returned, othervise false (including if they are eaqually
	 * good)
	 * @param compeditor the individual to compare to
	 * @return true if caller superior to compeditor
	 */
	public abstract boolean fitterThan(Individual compeditor);
}
