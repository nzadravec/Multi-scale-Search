package hr.fer.zemris.zavrad.simulation;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.Random;

import hr.fer.zemris.zavrad.AlgorithmMACRO_H;
import hr.fer.zemris.zavrad.function.IFunction;
import hr.fer.zemris.zavrad.function.SBBAFunction;
import hr.fer.zemris.zavrad.function.TestFunction;
import hr.fer.zemris.zavrad.solution.BitvectorSolution;
import hr.fer.zemris.zavrad.util.ArraysUtil;

public class SBBAFunctionTest {

	private static int NUM_OF_INDEPENDENT_REPEATS = 30;

	public static void main(String[] args) throws UnsupportedEncodingException, FileNotFoundException, IOException {

		try (Writer writer = getWriter(args[0])) {

			Random rand = new Random();

			for (int k = 5; k <= 11; k++) {
				System.out.println("k: " + k);

				int partLength = k;
				int numOfParts = k;

				boolean[] target1 = new boolean[numOfParts * partLength];
				boolean[] target2 = new boolean[numOfParts * partLength];

				BitvectorSolution global = new BitvectorSolution(numOfParts * partLength);
				TestFunction testFunction = new TestFunction();

				for (int j = 0; j < NUM_OF_INDEPENDENT_REPEATS; j++) {
					System.out.println("j: "+j);

					ArraysUtil.flipBitArray(target1, rand);
					ArraysUtil.flipBitArray(target2, rand);

					IFunction function = new SBBAFunction(numOfParts, partLength, target1, target2);
					int numOfLocalSearches = (int) Math.ceil(10.43 * Math.log(function.numberOfVariables()));

					for (int i = 0, n = function.numberOfVariables(); i < n; i++) {
						global.set(i, target1[i]);
					}

					testFunction.setFunction(function);
					new AlgorithmMACRO_H(testFunction, numOfLocalSearches, true, global).go();

				}

				double meanEvaluations = testFunction.numberOfEvaluations() / NUM_OF_INDEPENDENT_REPEATS;
				writer.write(Math.pow(k, 2) + " " + meanEvaluations + System.getProperty("line.separator"));

			}
		}

	}

	private static BufferedWriter getWriter(String fileName)
			throws UnsupportedEncodingException, FileNotFoundException {
		return new BufferedWriter(new OutputStreamWriter(
				new BufferedOutputStream(new FileOutputStream(".\\data\\" + fileName + ".dat")), "UTF-8"));
	}

}
