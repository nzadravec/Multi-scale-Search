package hr.fer.zemris.zavrad.function;

import java.util.Arrays;

import hr.fer.zemris.zavrad.util.ArraysUtil;

/**
 * 
 * 
 * @author Nikola Zadravec
 *
 */
public class ConcatenatedTrapFunction implements IFunction {

	private TrapFunction[] traps;
	private int numOfParts;
	private int partLength;
	
	private boolean[] globalOptimum;

	public ConcatenatedTrapFunction(int numOfParts, int partLength) {
		this.numOfParts = numOfParts;
		this.partLength = partLength;

		traps = new TrapFunction[numOfParts];
		int[] mapping = ArraysUtil.linearFillArray(new int[numOfParts * partLength]);

		int slopeChangeLocation = (int) Math.floor(3 * partLength / (double) 4);
		for (int i = 0; i < numOfParts; i++) {
			traps[i] = new TrapFunction(partLength, 1, 2, slopeChangeLocation,
					Arrays.copyOfRange(mapping, i * partLength, (i + 1) * partLength));
		}
		
		this.globalOptimum = new boolean[numOfParts * partLength];
		for (int i = 0; i < numOfParts; i++) {

			boolean[] go = traps[i].globalOptimum();
			for (int j = 0; j < partLength; j++) {
				globalOptimum[partLength * i + j] = go[j];
			}
		}
		
	}

	public int getNumOfParts() {
		return numOfParts;
	}

	public int getPartLength() {
		return partLength;
	}

	@Override
	public int numberOfVariables() {
		return numOfParts * partLength;
	}

	@Override
	public double evaluate(boolean[] variables) {
		double value = 0;
		for (TrapFunction trap : traps) {
			value += trap.evaluate(variables);
		}

		return value;
	}

	@Override
	public boolean[] globalOptimum() {
		return globalOptimum;
	}

}
