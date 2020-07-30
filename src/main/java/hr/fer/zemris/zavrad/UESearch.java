package hr.fer.zemris.zavrad;

import java.util.List;

import hr.fer.zemris.zavrad.function.IFunction;
import hr.fer.zemris.zavrad.solution.BitvectorSolution;

public abstract class UESearch {

	protected IFunction function;
	protected List<Unit> units;

	public UESearch(IFunction function, List<Unit> unitSet) {
		this.function = function;
		this.units = unitSet;
	}
	
	abstract BitvectorSolution search();
	
}
