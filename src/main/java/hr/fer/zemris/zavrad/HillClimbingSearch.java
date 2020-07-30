package hr.fer.zemris.zavrad;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import hr.fer.zemris.zavrad.function.IFunction;
import hr.fer.zemris.zavrad.solution.BitvectorSolution;

public class HillClimbingSearch extends UESearch {

	public HillClimbingSearch(IFunction function, List<Unit> unitSet) {
		super(function, unitSet);
	}

	public BitvectorSolution search() {

		// Construct initial point x randomly based on setUnit
		BitvectorSolution solution = new BitvectorSolution(function.numberOfVariables());

		boolean[] flags = new boolean[function.numberOfVariables()];
		int counter = 0;
		while (counter < function.numberOfVariables()) {

			Collections.shuffle(units);
			for (Unit u : units) {
				for (int i = 0, n = u.getSize(); i < n; i++) {

					if (!flags[i] && u.getMemberAt(i)) {
						flags[i] = true;
						solution.set(i, u.getAssignmentAt(i));
						counter++;
					}
				}
			}
		}
		
		solution.setFitness(function.evaluate(solution.getBitvector()));

		Collections.shuffle(units);
		Iterator<Unit> iterator = units.iterator();
		boolean flag = true;

		while (true) {
			// Create y as random 'neighbor' of x
			BitvectorSolution neighbor = solution.duplicate();
			while (neighbor.equals(solution)) {

				if (iterator.hasNext()) {

					Unit u = iterator.next();
					for (int i = 0, n = u.getSize(); i < n; i++) {
						if (u.getMemberAt(i)) {
							neighbor.set(i, u.getAssignmentAt(i));
						}
					}

				} else {

					if (flag) {
						return solution;
					}

					Collections.shuffle(units);
					iterator = units.iterator();
					flag = true;
				}
			}
			
			neighbor.setFitness(function.evaluate(neighbor.getBitvector()));
			
			// Select better search point
			if (neighbor.getFitness() >= solution.getFitness()) {
				solution = neighbor;
				flag = false;
			}

		}
	}
	
}
