package hr.fer.zemris.zavrad.function;

import java.util.Arrays;

import hr.fer.zemris.zavrad.util.ArraysUtil;

/**
 * Class represents (m,k)-separable function. It's the concatenated version of
 * {@link TwoMaxFunction}. Acronym stands for scalable building-block.
 * 
 * @author nikol
 *
 */
public class SBBFunction implements IFunction {

	private TwoMaxFunction[] twoMaxs;
	private int numOfParts;
	private int partLength;

	private boolean[] globalOptimum;

	public SBBFunction(int numOfParts, int partLength) {
		this(numOfParts, partLength, new boolean[numOfParts * partLength],
				ArraysUtil.linearFillArray(new int[numOfParts * partLength]));
	}

	public SBBFunction(int numOfParts, int partLength, boolean[] bitSwaps, int[] mapping) {
		this.numOfParts = numOfParts;
		this.partLength = partLength;

		twoMaxs = new TwoMaxFunction[numOfParts];

		for (int i = 0; i < numOfParts; i++) {
			twoMaxs[i] = new TwoMaxFunction(partLength, 1,
					Arrays.copyOfRange(bitSwaps, i * partLength, (i + 1) * partLength),
					Arrays.copyOfRange(mapping, i * partLength, (i + 1) * partLength));
		}

		this.globalOptimum = new boolean[numOfParts * partLength];
		for (int i = 0; i < numOfParts; i++) {

			boolean[] go = twoMaxs[i].globalOptimum();
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
		for (TwoMaxFunction twomax : twoMaxs) {
			value += twomax.evaluate(variables);
		}

		return value;
	}

	@Override
	public boolean[] globalOptimum() {
		return globalOptimum;
	}

}
