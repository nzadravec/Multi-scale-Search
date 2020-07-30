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

import org.jfree.data.xy.XYSeries;

import hr.fer.zemris.zavrad.function.IFunction;
import hr.fer.zemris.zavrad.solution.BitvectorSolution;

/**
 * An implementation of the MACRO-H algorithm to optimize (m,k)-separable
 * functions.
 * 
 * @author Nikola Zadravec
 *
 */
public class AlgorithmMACRO_H1 {

	private IFunction function;
	private int numOfLocalSearches;

	// unit transformation mechanism disabled
	private boolean disabled;
	private double globalFitness;

	private List<Unit> units;
	private List<BitvectorSolution> population;

	private boolean[][] associationMatrix;
	private Map<Unit, Integer> unitIndexMap;

	public AlgorithmMACRO_H1(IFunction function, int numOfLocalSearches, boolean disabled) {

		this.function = function;
		this.numOfLocalSearches = numOfLocalSearches;
		this.disabled = disabled;
		this.globalFitness = function.evaluate(function.globalOptimum());

		unitIndexMap = new HashMap<>();
		population = new ArrayList<>();
	}

	/**
	 * The start method of the algorithm.
	 * 
	 * @return best search point found
	 */
	public BitvectorSolution go() {

		initUnitSet();
		
		List<Jedinka> populacija = new ArrayList<>();
		
		for (int i = 0; i < numOfLocalSearches; i++) {
			BitvectorSolution point = getRandomPoint();
			point.setFitness(function.evaluate(point.getBitvector()));
			population.add(point);
			populacija.add(new Jedinka(point));
			Jedinka jedinka = populacija.get(i);
			jedinka.series.add(jedinka.timestep, jedinka.point.getFitness());
			jedinka.timestep++;
		}

		boolean breakFlag = false;
		int count = 0;
		while (true) {

			System.out.println("count:"+count++);
			
			for (int i = 0; i < numOfLocalSearches; i++) {
				UESearch(populacija.get(i));
			}
			
			if (!disabled) {
				updateAssociationMatrix();
				updateUnitSet();
			}
				
			for(BitvectorSolution point : population) {
				
				if (point.getFitness() == globalFitness) {
					breakFlag = true;
					break;
				}
			}
			
			if(breakFlag) {
				break;
			}

		}

		BitvectorSolution best = population.get(0);
		for (int i = 1, n = population.size(); i < n; i++) {
			if (best.getFitness() < population.get(i).getFitness()) {
				best = population.get(i);
			}
		}

		return best;
	}

	private BitvectorSolution getRandomPoint() {
		BitvectorSolution point = new BitvectorSolution(function.numberOfVariables());

		boolean[] flags = new boolean[function.numberOfVariables()];
		int counter = 0;
		while (counter < function.numberOfVariables()) {

			Collections.shuffle(units);
			for (Unit u : units) {
				for (int i = 0, n = u.getSize(); i < n; i++) {

					if (!flags[i] && u.getMemberAt(i)) {
						flags[i] = true;
						point.set(i, u.getAssignmentAt(i));
						counter++;
					}
				}
			}
		}
		
		return point;
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
			population.stream().filter(y -> u.agrees(y)).forEach(y -> agreesMap.get(u).add(y));
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

			Optional<BitvectorSolution> solution = population.stream().filter(y -> unit1.agrees(y)).findAny();
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
	private void UESearch(Jedinka jedinka) {
		BitvectorSolution solution = jedinka.point;
		
		Collections.shuffle(units);
		Iterator<Unit> iterator = units.iterator();
		boolean flag = true;

		//BitvectorSolution past;
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
						return;
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
				jedinka.series.add(jedinka.timestep, solution.getFitness());
				jedinka.timestep++;
				flag = false;
			}

		}
		
	}
	
	private static class Jedinka {
		
		private static int index;
		
		private BitvectorSolution point;
		private int timestep;
		final XYSeries series = new XYSeries(""+index++);
		
		public Jedinka(BitvectorSolution point) {
			super();
			this.point = point;
		}
		
	}

}
