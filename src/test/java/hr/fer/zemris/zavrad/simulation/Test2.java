package hr.fer.zemris.zavrad.simulation;

import java.util.Arrays;

import hr.fer.zemris.zavrad.AlgorithmMACRO_H;
import hr.fer.zemris.zavrad.function.SBBAFunction;
import hr.fer.zemris.zavrad.solution.BitvectorSolution;

public class Test2 {

	public static void main(String[] args) {

		int numOfParts = 2;
		int partLength = 12;
		boolean[] t1 = new boolean[numOfParts * partLength];
		Arrays.fill(t1, true);
		boolean[] t2 = new boolean[numOfParts * partLength];

		t2[1] = true;
		t2[10] = true;
		t2[19] = true;
		t2[22] = true;

		SBBAFunction sbba = new SBBAFunction(2, 12, t1, t2);
		int numOfLocalSearches = (int) Math.ceil(10.43 * Math.log(sbba.numberOfVariables()));
		BitvectorSolution global = new BitvectorSolution(sbba.globalOptimum());
		
		new AlgorithmMACRO_H(sbba, numOfLocalSearches, false, global).go();
		System.out.println("kraj");

	}

}
