package hr.fer.zemris.zavrad.function;

import java.util.Arrays;

import hr.fer.zemris.zavrad.util.ArraysUtil;

public class SBBAFunction implements IFunction {

	private ArbMaxFunction[] arbMaxs;
	private int numOfParts;
	private int partLength;
	
	private boolean[] globalOptimum;

	public SBBAFunction(int numOfParts, int partLength, boolean[] target1, boolean[] target2) {
		this.numOfParts = numOfParts;
		this.partLength = partLength;

		arbMaxs = new ArbMaxFunction[numOfParts];
		int[] mapping = ArraysUtil.linearFillArray(new int[numOfParts * partLength]);

		for (int i = 0; i < numOfParts; i++) {
			arbMaxs[i] = new ArbMaxFunction(partLength,
					Arrays.copyOfRange(target1, i * partLength, (i + 1) * partLength),
					Arrays.copyOfRange(target2, i * partLength, (i + 1) * partLength),
					Arrays.copyOfRange(mapping, i * partLength, (i + 1) * partLength));
		}
		
		this.globalOptimum = new boolean[numOfParts * partLength];
		for (int i = 0; i < numOfParts; i++) {

			boolean[] go = arbMaxs[i].globalOptimum();
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
		int value = 0;
		for (ArbMaxFunction arbMax : arbMaxs) {
			value += arbMax.evaluate(variables);
		}

		return value;
	}

	@Override
	public boolean[] globalOptimum() {
		return globalOptimum;
	}

}
