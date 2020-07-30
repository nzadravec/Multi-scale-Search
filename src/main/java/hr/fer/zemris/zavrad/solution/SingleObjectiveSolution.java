package hr.fer.zemris.zavrad.solution;

/**
 * 
 * 
 * @author Nikola Zadravec
 *
 */
public class SingleObjectiveSolution implements Comparable<SingleObjectiveSolution> {
	
	private double fitness;
	
	public double getFitness() {
		return fitness;
	}

	public void setFitness(double fitness) {
		this.fitness = fitness;
	}

	@Override
	public int compareTo(SingleObjectiveSolution other) {
		return Double.compare(this.fitness, other.fitness);
	}

}
