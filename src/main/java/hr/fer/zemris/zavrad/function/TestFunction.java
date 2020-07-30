package hr.fer.zemris.zavrad.function;

public class TestFunction implements IFunction {

	private IFunction function;
	private int numberOfEvaluations;
	
	public TestFunction() {
		
	}
	
	public TestFunction(IFunction function) {
		this.function = function;
	}
	
	public IFunction getFunction() {
		return function;
	}

	public void setFunction(IFunction function) {
		this.function = function;
	}

	public int numberOfEvaluations() {
		return numberOfEvaluations;
	}
	
	public void resetNumOfEvaluations() {
		numberOfEvaluations = 0;
	}

	@Override
	public int numberOfVariables() {
		return function.numberOfVariables();
	}

	@Override
	public double evaluate(boolean[] variables) {
		numberOfEvaluations++;
		return function.evaluate(variables);
	}

	@Override
	public boolean[] globalOptimum() {
		return function.globalOptimum();
	}
	
}
