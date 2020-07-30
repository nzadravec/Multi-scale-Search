package hr.fer.zemris.zavrad.simulation;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.Arrays;

import hr.fer.zemris.zavrad.AlgorithmMACRO_H;
import hr.fer.zemris.zavrad.function.ConcatenatedTrapFunction;
import hr.fer.zemris.zavrad.function.TestFunction;
import hr.fer.zemris.zavrad.solution.BitvectorSolution;

public class ConcatenatedTrapFunctionTest {

	private static int NUM_OF_INDEPENDENT_REPEATS = 100;
	private static int PART_LENGTH = 5;

	public static void main(String[] args) throws UnsupportedEncodingException, FileNotFoundException, IOException {

		try (Writer writer = getWriter(args[0])) {

			for (int numberOfVariables = 25; numberOfVariables <= 625; numberOfVariables += 50) {
				System.out.println("numberOfVariables: " + numberOfVariables);

				int numOfParts = numberOfVariables / PART_LENGTH;
				boolean[] bitvector = new boolean[numOfParts * PART_LENGTH];
				Arrays.fill(bitvector, true);
				BitvectorSolution global = new BitvectorSolution(bitvector);
				TestFunction testFunction = new TestFunction(new ConcatenatedTrapFunction(numOfParts, PART_LENGTH));
				int numOfLocalSearches = (int) Math.ceil(64 * Math.log(testFunction.numberOfVariables()));
				
				for (int j = 0; j < NUM_OF_INDEPENDENT_REPEATS; j++) {
					System.out.println("j: "+j);

					new AlgorithmMACRO_H(testFunction, numOfLocalSearches, false, global).go();

				}

				double meanEvaluations = testFunction.numberOfEvaluations() / NUM_OF_INDEPENDENT_REPEATS;
				writer.write(numberOfVariables + " " + meanEvaluations + System.getProperty("line.separator"));

			}
		}

	}

	private static BufferedWriter getWriter(String fileName)
			throws UnsupportedEncodingException, FileNotFoundException {
		return new BufferedWriter(new OutputStreamWriter(
				new BufferedOutputStream(new FileOutputStream(".\\data\\" + fileName + ".dat")), "UTF-8"));
	}

}
