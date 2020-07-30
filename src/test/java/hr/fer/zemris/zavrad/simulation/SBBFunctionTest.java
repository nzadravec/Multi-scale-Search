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
import hr.fer.zemris.zavrad.function.SBBFunction;
import hr.fer.zemris.zavrad.function.TestFunction;
import hr.fer.zemris.zavrad.solution.BitvectorSolution;
import hr.fer.zemris.zavrad.util.ArraysUtil;

public class SBBFunctionTest {

	private static int NUM_OF_INDEPENDENT_REPEATS = 100;

	/**
	 * Main program.
	 * 
	 * @param args
	 *            command line arguments - not used
	 * @throws IOException
	 * @throws FileNotFoundException
	 * @throws UnsupportedEncodingException
	 */
	public static void main(String[] args) throws UnsupportedEncodingException, FileNotFoundException, IOException {

		try (Writer writer = getWriter(args[0])) {

			Random rand = new Random();

			for (int k = 11; k <= 11; k++) {
				System.out.println("k: " + k);

				int partLength = k;
				int numOfParts = k;

				boolean[] bitSwaps = new boolean[numOfParts * partLength];
				int[] mapping = ArraysUtil.linearFillArray(new int[numOfParts * partLength]);
				BitvectorSolution global = new BitvectorSolution(numOfParts * partLength);
				TestFunction testFunction = new TestFunction();

				for (int j = 0; j < NUM_OF_INDEPENDENT_REPEATS; j++) {
					System.out.println("j: " + j);

					ArraysUtil.flipBitArray(bitSwaps, rand);
					ArraysUtil.shuffleArray(mapping, rand);

					IFunction function = new SBBFunction(numOfParts, partLength, bitSwaps, mapping);
					int numOfLocalSearches = (int) Math.ceil(10.43 * Math.log(function.numberOfVariables()));

					for (int i = 0, n = function.numberOfVariables(); i < n; i++) {
						global.set(mapping[i], true ^ bitSwaps[i]);
					}

					testFunction.setFunction(function);
					new AlgorithmMACRO_H(testFunction, numOfLocalSearches, true, global).go();
					System.exit(1);
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
