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
import hr.fer.zemris.zavrad.function.TestFunction;
import hr.fer.zemris.zavrad.solution.BitvectorSolution;

public abstract class FunctionTest {
	
	private TestFunction function;
	private int numOfLocalSearches;
	private boolean disabled;
	
	public FunctionTest(TestFunction function, int numOfLocalSearches, boolean disabled) {
		super();
		this.function = function;
		this.numOfLocalSearches = numOfLocalSearches;
		this.disabled = disabled;
	}
	
	public int test() {
		
		function.resetNumOfEvaluations();
		BitvectorSolution global = new BitvectorSolution(function.globalOptimum(), false);
		new AlgorithmMACRO_H(function, numOfLocalSearches, disabled, global).go();
		
		return function.numberOfEvaluations();
	}
	
	public static void test(String fileName) throws UnsupportedEncodingException, FileNotFoundException, IOException {
		
		try (Writer writer = getWriter(fileName)) {
			
			
			
		}

	}
	
	private static BufferedWriter getWriter(String fileName)
			throws UnsupportedEncodingException, FileNotFoundException {
		return new BufferedWriter(new OutputStreamWriter(
				new BufferedOutputStream(new FileOutputStream(".\\data\\" + fileName + ".dat")), "UTF-8"));
	}
	
}
