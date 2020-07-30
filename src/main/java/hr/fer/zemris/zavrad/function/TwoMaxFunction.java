package hr.fer.zemris.zavrad.function;

import hr.fer.zemris.zavrad.util.ArraysUtil;

/**
 * Class represents a function that is almost symmetrical with one global and
 * one local optimum, one the all-ones bits string, the other the all-zeros bit
 * string.
 * 
 * @author Nikola Zadravec
 *
 */
public class TwoMaxFunction implements IFunction {

	private int numberOfVariables;
	private int constantC;

	private boolean[] bitSwaps;
	private int[] mapping;
	private boolean[] globalOptimum;

	public TwoMaxFunction(int numberOfVariables, int constantC, boolean[] bitSwaps, int[] mapping) {
		this.numberOfVariables = numberOfVariables;
		this.constantC = constantC;
		this.bitSwaps = ArraysUtil.copyOf(bitSwaps);
		this.mapping = ArraysUtil.copyOf(mapping);
		
		this.globalOptimum = new boolean[numberOfVariables];
		for (int i = 0; i < numberOfVariables; i++) {
			globalOptimum[i] = true ^ bitSwaps[i];
		}
	}

	@Override
	public int numberOfVariables() {
		return numberOfVariables;
	}

	@Override
	public double evaluate(boolean[] variables) {
		int numOfOnes = 0;
		boolean allOnes = true;
		for (int i = 0; i < numberOfVariables; i++) {

			if (variables[mapping[i]] ^ bitSwaps[i]) {
				numOfOnes++;
			} else {
				allOnes = false;
			}
		}

		int value = Math.max(numOfOnes, numberOfVariables - numOfOnes);

		if (allOnes) {
			value += constantC;
		}

		return value;
	}

	@Override
	public boolean[] globalOptimum() {
		return globalOptimum;
	}
	
	public static int evaluate(boolean[] variables, int start, int end) {
		int numOfOnes = 0;
		boolean allOnes = true;
		for (int i = start; i < end; i++) {

			if (variables[i]) {
				numOfOnes++;
			} else {
				allOnes = false;
			}
		}

		int value = Math.max(numOfOnes, end - start - numOfOnes);

		if (allOnes) {
			value += 1;
		}

		return value;
	}

}
