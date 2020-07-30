package hr.fer.zemris.zavrad.function;

/**
 * An interface that describes a function that optimizes.
 * 
 * @author Nikola Zadravec
 *
 */
public interface IFunction {

	int numberOfVariables();
    double evaluate(boolean[] variables);
    boolean[] globalOptimum();
	
}
