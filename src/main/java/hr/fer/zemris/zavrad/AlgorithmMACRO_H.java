package hr.fer.zemris.zavrad;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import hr.fer.zemris.zavrad.function.IFunction;
import hr.fer.zemris.zavrad.solution.BitvectorSolution;

/**
 * An implementation of the MACRO-H algorithm to optimize (m,k)-separable
 * functions.
 * 
 * @author Nikola Zadravec
 *
 */
public class AlgorithmMACRO_H {

	private IFunction function;
	private int numOfLocalSearches;

	// unit transformation mechanism disabled
	private boolean disabled;
	private BitvectorSolution global;

	private List<Unit> units;
	private List<BitvectorSolution> bestLocalPoints;

	private boolean[][] associationMatrix;
	private Map<Unit, Integer> unitIndexMap;

	public AlgorithmMACRO_H(IFunction function, int numOfLocalSearches, boolean disabled, BitvectorSolution global) {

		this.function = function;
		this.numOfLocalSearches = numOfLocalSearches;
		this.disabled = disabled;
		this.global = global;

		unitIndexMap = new HashMap<>();
		bestLocalPoints = new ArrayList<>();
	}

	/**
	 * The start method of the algorithm.
	 * 
	 * @return best search point found
	 */
	public BitvectorSolution go() {

		initUnitSet();

		int counter = 0;

		while (true) {

			counter++;
			if(counter % 1000 == 0) {
				System.out.println(counter);
			}

			long start = System.nanoTime();
			bestLocalPoints.clear();
			for (int i = 0; i < numOfLocalSearches; i++) {
				BitvectorSolution point = UESearch();
				bestLocalPoints.add(point);
			}
			long end = System.nanoTime();
			System.out.println("1: "+(end-start));
			
			if (!disabled) {
				
				start = System.nanoTime();
				updateAssociationMatrix();
				end = System.nanoTime();
				System.out.println("2: "+(end-start));
				
				start = System.nanoTime();
				updateUnitSet();
				end = System.nanoTime();
				System.out.println("3: "+(end-start));
			}
				
			if (bestLocalPoints.contains(global)) {
				break;
			}

		}

		BitvectorSolution best = bestLocalPoints.get(0);
		for (int i = 1, n = bestLocalPoints.size(); i < n; i++) {
			if (best.getFitness() < bestLocalPoints.get(i).getFitness()) {
				best = bestLocalPoints.get(i);
			}
		}

		return best;
	}

	/**
	 * Initialize unit set with all primitive units (that is to say, one unit
	 * for each value of each problem variable).
	 */
	private void initUnitSet() {
		units = new ArrayList<>();
		boolean[] members = new boolean[function.numberOfVariables()];
		boolean[] assignment = new boolean[function.numberOfVariables()];

		for (int i = 0, n = members.length; i < n; i++) {
			members[i] = true;
			Unit u = new Unit(members, assignment, true);
			unitIndexMap.put(u, units.size());
			units.add(u);
			members[i] = false;
		}

		Arrays.fill(assignment, true);
		for (int i = 0, n = members.length; i < n; i++) {
			members[i] = true;
			Unit u = new Unit(members, assignment, true);
			unitIndexMap.put(u, units.size());
			units.add(u);
			members[i] = false;
		}

	}

	/**
	 * Updates co-occurrence measure, association matrix, based on on
	 * correlations between units present in these optima.
	 */
	private void updateAssociationMatrix() {

		Map<Unit, Set<BitvectorSolution>> agreesMap = new HashMap<>();
		units.stream().forEach(u -> agreesMap.put(u, new HashSet<>()));

		for (Unit u : units) {
			bestLocalPoints.stream().filter(y -> u.agrees(y)).forEach(y -> agreesMap.get(u).add(y));
		}

		associationMatrix = new boolean[units.size()][units.size()];
		for (Unit u : units) {
			for (Unit v : units) {

				if (u.equals(v)) {
					continue;
				}

				if (agreesMap.get(u).size() > 0 && agreesMap.get(u).equals(agreesMap.get(v))) {
					
					associationMatrix[unitIndexMap.get(u)][unitIndexMap.get(v)] = true;
					associationMatrix[unitIndexMap.get(v)][unitIndexMap.get(u)] = true;
				}

			}
		}
	}

	/**
	 * Update unit set by combining the most strongly correlated units into
	 * composite units.
	 */
	private void updateUnitSet() {
		List<Unit> newUnitSet = new ArrayList<>();
		Map<Unit, Integer> newUnitIndexMap = new HashMap<>();

		while (!units.isEmpty()) {
			Iterator<Unit> iterator1 = units.iterator();
			Unit unit1 = iterator1.next();
			iterator1.remove();

			Optional<BitvectorSolution> solution = bestLocalPoints.stream().filter(y -> unit1.agrees(y)).findAny();
			if (solution.isPresent()) {
				// construct group
				Unit group = unit1.duplicate();

				Collections.shuffle(units);
				Iterator<Unit> iterator2 = units.iterator();
				while (iterator2.hasNext()) {

					Unit unit2 = iterator2.next();

					if (associationMatrix[unitIndexMap.get(unit1)][unitIndexMap.get(unit2)]) {

						group.groupConstruction(unit2);
						iterator2.remove();
					}
				}

				newUnitIndexMap.put(group, newUnitSet.size());
				newUnitSet.add(group);
			}
//			} else {
//				newUnitIndexMap.put(unit1, newUnitSet.size());
//				newUnitSet.add(unit1);
//			}
		}

		units = newUnitSet;
		unitIndexMap = newUnitIndexMap;
	}

	/**
	 * Perform unit-exploiting search multiple times using the current
	 * variational units to ﬁnd several different local optima (“locally
	 * optimal” with respect to the current variational units).
	 * 
	 * @return best local search point
	 */
	private BitvectorSolution UESearch() {
		return new HillClimbingSearch(function, units).search();
	}

}
