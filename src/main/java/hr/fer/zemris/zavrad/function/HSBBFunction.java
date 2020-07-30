package hr.fer.zemris.zavrad.function;

public class HSBBFunction implements IFunction {

	private int initialBlockSize; // at the bottom level
	private int numberOfLayers;
	private int numberOfVariables;
	
	private boolean[] globalOptimum;

	public HSBBFunction(int initialBlockSize, int numberOfLayers) {
		this.initialBlockSize = initialBlockSize;
		this.numberOfLayers = numberOfLayers;
		
		numberOfVariables = (int) Math.pow(initialBlockSize, numberOfLayers);
		
		this.globalOptimum = new boolean[numberOfVariables];
		for (int i = 0; i < numberOfVariables; i++) {
			globalOptimum[i] = true;
		}
	}

	public double evaluate(boolean[] variables) {
		double value = 0;
		int layerFactor = 1;
		int blockSize = initialBlockSize;

		for (int layer = 1; layer <= numberOfLayers; layer++) {

			for (int i = 0; i < numberOfVariables; i += blockSize) {
				value += layerFactor * f(variables, i, i + blockSize);
			}

			layerFactor *= initialBlockSize;
			blockSize *= initialBlockSize;
		}

		return value;
	}
	
	public static int f(boolean[] variables, int start, int end) {
		
		if(transform(variables, start, end) == null) {
			return 0;
		}
		
		return TwoMaxFunction.evaluate(variables, start, end);
	}
	
	public static boolean[] transform(boolean[] variables, int start, int end) {
		boolean allOnes = true;
		boolean allZeros = true;
		
		for(int i = start; i < end; i++) {
			if(variables[i] == true) {
				allZeros = false;
			} else {
				allOnes = false;
			}
			
			if(!allOnes && !allZeros) {
				return null;
			}
		}
		
		return variables;
	}

	@Override
	public int numberOfVariables() {
		return numberOfVariables;
	}

	@Override
	public boolean[] globalOptimum() {
		return globalOptimum;
	}

}
