package algoritmogenetico;
/**
 * An interface between a Genetic Algorithm and its observers.
 * That is an object of type GeneticAlgorithmInfo will be sent
 * when the observable GeneticAlgorithm notifies its observers<br><br>
 * 
 * Lastest Modification: 11.12.2003
 * 
 * @author Hanna Sjolinder, NUIG student no 03103854
 */
public class GeneticAlgorithmInfo {
	
	private Individual fittestIndividual;
	private int generationNumber; 
	
	public GeneticAlgorithmInfo(Individual fittestIndividual,
								int generationNumber){
		this.fittestIndividual = fittestIndividual;
		this.generationNumber = generationNumber;
	}
	
	public Individual getFittestIndividual(){
		return fittestIndividual;
	}
	public int getGenerationNumber(){
		return generationNumber;
	}
}
