package algoritmogenetico;


import java.util.*;

/**
 * An interface that any class claming to be a genotype 
 * should implement.<br><br>
 * 
 * Lastest Modification: 19.11.2003
 * 
 * @author Hanna Sjolinder, NUIG student no 03103854
 */
public interface Genotype {
	/**
	 * mutates the gene
	 */
	public abstract void mutate(int mutationType);
	
	/**
	 * recombines this gene with another to create an offspring<br>
	 * @param recombineType specifies the type of mutation
	 * @param otherParent the parent to combine this gene with
	 * @return a vector with the resuting genes
	 */
	public abstract Vector recombine(int recombineType, Genotype otherParent);
	
	/**
	 * set this genotype to be a random gene
	 * used when initializing populations
	 */
	public abstract void randomize();
	
	public abstract String toString(); // useful eg for testing purposes
}
