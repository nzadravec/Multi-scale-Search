package hr.fer.zemris.zavrad.function;

import hr.fer.zemris.zavrad.util.ArraysUtil;

public class ArbMaxFunction implements IFunction {

	private int numberOfVariables;
	private boolean[] target1;
	private boolean[] target2;
	private int[] mapping;
	
	private boolean[] globalOptimum;

	public ArbMaxFunction(int numberOfVariables, boolean[] target1, boolean[] target2, int[] mapping) {
		super();
		this.numberOfVariables = numberOfVariables;
		this.target1 = ArraysUtil.copyOf(target1);
		this.target2 = ArraysUtil.copyOf(target2);
		this.mapping = ArraysUtil.copyOf(mapping);
		
		this.globalOptimum = ArraysUtil.copyOf(target1);
	}

	@Override
	public int numberOfVariables() {
		return numberOfVariables;
	}

	@Override
	public double evaluate(boolean[] variables) {
		int distance1 = 0;
		int distance2 = 0;
		boolean equivalence = true;

		for (int i = 0; i < numberOfVariables; i++) {

			if (target1[i] != variables[mapping[i]]) {
				distance1++;
				equivalence = false;
			}
			if (target2[i] != variables[mapping[i]]) {
				distance2++;
			}
		}

		int value = Math.max(numberOfVariables - distance1, numberOfVariables - distance2);

		if(equivalence) {
			value += 1;
		}
		
		return value;
	}

	@Override
	public boolean[] globalOptimum() {
		return globalOptimum;
	}

}
