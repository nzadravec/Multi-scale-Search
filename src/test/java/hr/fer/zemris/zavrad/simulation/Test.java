package hr.fer.zemris.zavrad.simulation;

import java.util.Arrays;

import hr.fer.zemris.zavrad.function.TrapFunction;
import hr.fer.zemris.zavrad.util.ArraysUtil;

public class Test {

	public static void main(String[] args) {

		int numberOfVariables = 10;

		int[] mapping = ArraysUtil.linearFillArray(new int[numberOfVariables]);

		int slopeChangeLocation = (int) Math.floor(3 * numberOfVariables / (double) 4);
		// for (int i = 0; i < numOfParts; i++) {
		// traps[i] = new TrapFunction(partLength, 1, 2, slopeChangeLocation,
		// Arrays.copyOfRange(mapping, i * partLength, (i + 1) * partLength));
		// }

		TrapFunction f = new TrapFunction(numberOfVariables, 1, 2, slopeChangeLocation, mapping);

		boolean[] solution = new boolean[numberOfVariables];
		boolean[] best = solution;
		for (int i = 0, n = (int) Math.pow(2, numberOfVariables); i < n; i++) {
			
			int input = i;
			
		    for (int j = 9; j >= 0; j--) {
		    	solution[j] = (input & (1 << j)) != 0;
		    }
		    
		    System.out.println(i+": "+Arrays.toString(solution));

			if (f.evaluate(solution) > f.evaluate(best)) {
				best = solution;
			}

		}
		
		System.out.println(Arrays.toString(best));

	}

}
