package hr.fer.zemris.zavrad.function;

import java.util.Arrays;

import hr.fer.zemris.zavrad.util.ArraysUtil;

/**
 * The string with all ones and the string with all zeros are assumed to be the
 * globally and the locally optimal strings respectively, and the unitation is
 * defined as the number of ones in a string.
 * 
 * @author Nikola Zadravec
 *
 */
public class TrapFunction implements IFunction {

	private int numberOfVariables;
	private double constantA;
	private double constantB;
	private int slopeChangeLocation;

	private int[] mapping;
	private boolean[] globalOptimum;

	public TrapFunction(int numberOfVariables, double constantA, double constantB, int slopeChangeLocation,
			int[] mapping) {
		this.numberOfVariables = numberOfVariables;
		this.constantA = constantA;
		this.constantB= constantB;
		this.slopeChangeLocation = slopeChangeLocation;
		this.mapping = ArraysUtil.copyOf(mapping);
		
		this.globalOptimum = new boolean[numberOfVariables];
		Arrays.fill(globalOptimum, true);
	}

	@Override
	public int numberOfVariables() {
		return numberOfVariables;
	}

	@Override
	public double evaluate(boolean[] variables) {
		double value;

		int unitation = 0; // number of ones in a string

		for (int i = 0; i < numberOfVariables; i++) {
			if (variables[mapping[i]]) {
				unitation++;
			}
		}

		if (unitation <= slopeChangeLocation) {
			value = (constantA / slopeChangeLocation) * (slopeChangeLocation - unitation);
		} else {
			value = (constantB / (numberOfVariables - slopeChangeLocation)) * (unitation - slopeChangeLocation);
		}

		return value;
	}

	@Override
	public boolean[] globalOptimum() {
		return globalOptimum;
	}

}
