package algoritmogenetico;

import java.util.*;

/**
 * Represents a permutation genotype implemented by
 * an inner array bla bla or something<br><br>
 * 
 * Lastest Modification: 11.12.2004
 *
 * @author Hanna Sjolinder, NUIG student no 03103854
 */
public class PermutationGenotype implements Genotype {
	
	// defining constants 
	public static final int SWAP_MUTATION = 0;
	public static final int PMX_RECOMBINE = 0;
	
	private int[] gene;
	private static int geneLength;
	
	// parameters
	static boolean debug = false;
	
	// constructor
	public PermutationGenotype(){
		gene = new int[geneLength];
	}
	
	// accessors
	public static void setGeneLength(int length){
		geneLength = length;
	}
	public void setGene( int[] gene){
		this.gene = gene;
	}
	public int[] getGene(){
		return gene;
	}
	
	/**
	 * mutates this gene according to mutationType.
	 * Currently there is only one type of muation,
	 * swap mutation.
	 * @param mutationType what mutation to perform
	 */
	public void mutate(int mutationType){
		switch(mutationType){
			case SWAP_MUTATION:
			swap_mutate();
			break;
			default:
			//do nothing
		}
	}
	/**
	 * picks two random gene elements and swaps them
	 */
	private void swap_mutate(){
		int index1, index2, temp;
		
		// make sure two diffrent elements are selected
		index1 = (int) ( Math.random() * geneLength );
		do{
			index2 = (int) ( Math.random() * geneLength );
		}while(index1==index2);
		
		// swap 'em
		temp=gene[index1];
		gene[index1]=gene[index2];
		gene[index2]=temp;
	}
	
	/**
	 * Recombines this and another gene. Only one type of
	 * recombination is currently availible PMX recombination
	 * 
	 * @param recombineType type of recombination to use
	 * @param otherParent gene of parent to recombine with
	 * @return a vector consisting of the two offspring
	 */
	public Vector recombine(int recombineType, Genotype otherParent){
		// make sure otherParent is of same type (try-catch)
		// recombine the two parents and return the offspring
		switch(recombineType){
			case PMX_RECOMBINE:
				return pmx_recombine((PermutationGenotype)otherParent);
			default:
				// do nothing
				return new Vector();
		}

	}
	/**
	 * Returns a vector containing two children constructed with pmx
	 * recombination from this genotype and the otherParent
	 * @param otherParent
	 * @return a vector containing the two offspring
	 */
	private Vector pmx_recombine(PermutationGenotype otherParent){
		int[] otherGene = otherParent.getGene();
		
		// chose two random (different) crossover points
		int crossover1, crossover2;
		crossover1 = (int) ( Math.random() * geneLength );
		do{
			crossover2 = (int) ( Math.random() * geneLength );
		}while(crossover1==crossover2);

		// for clearness swap them so that crossover1 < crossover2
		if( crossover1 > crossover2){
			int temp;
			temp = crossover1;
			crossover1 = crossover2;
			crossover2 = temp; 
		}		
		if(debug){
			System.out.println("crossover from: " + crossover1 + " to: " + crossover2);
		}
		
		// create 2 childs with parents order reveresed
		Genotype child1 = pmx_recombine_worker(	gene, 
												otherGene, 
												crossover1,
												crossover2);
		Genotype child2 = pmx_recombine_worker(	otherGene, 
												gene, 
												crossover1,
												crossover2);
												
		Vector offspring = new Vector(2);
		offspring.add(child1);
		offspring.add(child2);
		
		if(debug){
			System.out.println("child: " + offspring.elementAt(0).toString());
			System.out.println("child: " + offspring.elementAt(1).toString());
		}
		
		return offspring;

	}
	/**
	 * Returns a genotype consisting of gene as first and otherGene
	 * as second parent. This method is called twice with genes in
	 * opposite order by pmx_recombine() to get two parents
	 */
	private Genotype pmx_recombine_worker(	int[] gene, 
											int[] otherGene, 
											int crossover1,
											int crossover2){
		// create an array to hold the child gene,
		// set all element -1. This will later be used as
		// a flg for unset allele.
		int[] childGene = new int[geneLength];
		for(int i=0; i<geneLength; i++)
			childGene[i] = -1;


		// step1.
		// copy segment bw crossover points in first parent
		// gene into childgene1. Take note on what elements
		// are transfered + get a list of element that only
		// occur in the segment from the first parent
		Vector fromFirstParent = new Vector();
		Vector onlyInFirstParent = new Vector();
		for(int i=crossover1, j=0; i<=crossover2; i++,j++){
			childGene[i] = gene[i];
			fromFirstParent.add(new Integer(gene[i]));
		}
		boolean inSecondParentToo = false;
		for(int i=crossover1; i<=crossover2; i++){
			for(int j=crossover1; j<=crossover2; j++){
				if( (gene[i] == otherGene[j]) ){
					inSecondParentToo = true;
				}
			}
			if(!inSecondParentToo){
				onlyInFirstParent.add(new Integer(gene[i]));
			}
			inSecondParentToo = false;
		}

		if(debug){
			System.out.println("from first parent: " + fromFirstParent.toString());
			System.out.println("only in first parent: " + onlyInFirstParent.toString());
		}

		// step2.
		// copy those elements in the segment of second parent 
		// that are not in the segment for the first parent.
		// place these elements on the places that the elements that were
		// present in the first parents segment, that is there places
		// in the second parent ie outside the segment
		Iterator placeIt = onlyInFirstParent.iterator();
		for(int i=crossover1; i<=crossover2; i++){
			// make sure the current element is not already transferred
			if( !fromFirstParent.contains(new Integer(otherGene[i])) ){
				// find a place for the element
				int element = ((Integer)placeIt.next()).intValue();
				int indexOfElement=-1;
				for(int j=0; j<geneLength; j++){
					if(otherGene[j]==element){
						indexOfElement=j;
					}
				}
				// place the element
				childGene[indexOfElement] = otherGene[i];
				if(debug)
					System.out.println("place " + otherGene[i] + " at " + indexOfElement);
			}
		}

		// step3.
		// take the rest of the childgene from the second parent
		for(int i=0; i<geneLength; i++){
			if( childGene[i] == -1){
				childGene[i] = otherGene[i];
			}
		}

		PermutationGenotype resultGene = new PermutationGenotype();
		resultGene.setGene(childGene);

		return resultGene;											
	}
	/**
	 * randomizes this gene
	 */
	public void randomize(){		
		// create a Vector to represent an ordered 
		// array eg [1,2,3,4,...,geneLength]
		Vector orderedVector = new Vector();
		for(int i=0; i<geneLength; i++){
			orderedVector.add(new Integer(i));
		}
		
		// get a random index to the ordered Vector,
		// place this random selected integer into
		// the gene and hereafter remove that integer
		// from the Vector (since no number occurs 
		// more than once in a permutation)
		for(int i=0; i<geneLength; i++){
			int randIndex = (int) ( Math.random() * orderedVector.size() );
			gene[i] = ((Integer)(orderedVector.elementAt(randIndex))).intValue(); 
			orderedVector.remove(randIndex);
		}	
	}
	
	/**
	 * @return an iterator for the gene
	 */
	public Iterator iterator(){
		return new PermGeneIterator();
	}
	
	/**
	 * @return 	string representation of the gene. for example with
	 * 			gene length 6 this could be "[ 4 0 5 3 1 2 ]"
	 */
	public String toString(){
		String str = "[";
		for(int i=0; i<geneLength; i++){
			str += " " + gene[i];
		}
		str += " ]";
		return str;
	}
	
	/**
	 * Represents an iterator for the permutation genotype
	 * does not support the remove() method sice the gene
	 * is considered to be immutable
	 */
	private class PermGeneIterator implements Iterator{
		private int current = 0;
		public boolean hasNext(){
			if(current==gene.length)
				return false;
			else
				return true;
		}
		public Object next(){
			current++;
			return new Integer(gene[current-1]);
		}
		public void remove(){
			// not supported
			throw(new UnsupportedOperationException());
		}
	}
	
	/**
	 * for debug/test
	 */
	public static void main(String[] args) {
		debug = true;
		
		// test the pmx_recombine() method
		// create two PermutationGenopypes
		setGeneLength(8);
		
		PermutationGenotype parent1 = new PermutationGenotype();
		PermutationGenotype parent2 = new PermutationGenotype();	
		int[] gene1 = {1,2,3,4,5,6,7,8};
		int[] gene2 = {8,7,6,5,4,3,2,1};
		parent1.setGene(gene1);
		parent2.setGene(gene2);

		System.out.println("parent1: " + parent1.toString());
		System.out.println("parent2: " + parent2.toString());
		
		Vector children = parent1.recombine(0, parent2);
		System.out.println("child: " + children.elementAt(0).toString());
		System.out.println("child: " + children.elementAt(1).toString());	
	}
	
}
