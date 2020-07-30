package hr.fer.zemris.zavrad.simulation;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

import hr.fer.zemris.zavrad.AlgorithmMACRO_H;
import hr.fer.zemris.zavrad.AlgorithmMACRO_H1;
import hr.fer.zemris.zavrad.function.HSBBFunction;
import hr.fer.zemris.zavrad.function.IFunction;
import hr.fer.zemris.zavrad.function.TestFunction;
import hr.fer.zemris.zavrad.solution.BitvectorSolution;

public class HSBBFunctionTest {

	private static int NUM_OF_INDEPENDENT_REPEATS = 30;

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

			int numberOfLayers = 3;

			for (int k = 3; k <= 12; k++) {
				System.out.println("k: " + k);
				
				int initialBlockSize = k;
				IFunction function = new HSBBFunction(initialBlockSize, numberOfLayers);
				BitvectorSolution global = new BitvectorSolution(function.globalOptimum());
				TestFunction testFunction = new TestFunction(function);

				for (int j = 0; j < NUM_OF_INDEPENDENT_REPEATS; j++) {
					System.out.println("j: " + j);

					int numOfLocalSearches = (int) Math.ceil(10.43 * Math.log(function.numberOfVariables()));
					new AlgorithmMACRO_H1(testFunction, numOfLocalSearches, true).go();
					System.out.println("kraj!");
					System.exit(1);
				}

				double meanEvaluations = testFunction.numberOfEvaluations() / NUM_OF_INDEPENDENT_REPEATS;
				writer.write(function.numberOfVariables() + " " + meanEvaluations + System.getProperty("line.separator"));

			}

		}

	}

	private static BufferedWriter getWriter(String fileName)
			throws UnsupportedEncodingException, FileNotFoundException {
		return new BufferedWriter(new OutputStreamWriter(
				new BufferedOutputStream(new FileOutputStream(".\\data\\" + fileName + ".dat")), "UTF-8"));
	}
	
}
