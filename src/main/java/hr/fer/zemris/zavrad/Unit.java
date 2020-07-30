package hr.fer.zemris.zavrad;

import java.util.Arrays;

import hr.fer.zemris.zavrad.solution.BitvectorSolution;

public class Unit {

	private boolean[] members;
	private boolean[] assignment;

	public Unit(int numberOfBits) {
		members = new boolean[numberOfBits];
		assignment = new boolean[numberOfBits];
	}

	public Unit(boolean[] members, boolean[] assignment, boolean copyArrays) {
		if (members.length != assignment.length) {
			throw new IllegalArgumentException();
		}

		if (copyArrays) {
			copyArrays(members, assignment);

		} else {
			this.members = members;
			this.assignment = assignment;
		}
	}

	private void copyArrays(boolean[] members, boolean[] assignment) {
		this.members = new boolean[members.length];
		this.assignment = new boolean[members.length];

		for (int i = 0, n = members.length; i < n; i++) {
			if (members[i]) {
				this.members[i] = true;

				if (assignment[i]) {
					this.assignment[i] = true;
				}
			}
		}
	}

	public Unit duplicate() {
		return new Unit(members, assignment, true);
	}

	public boolean getMemberAt(int index) {
		return members[index];
	}
	
	public boolean getAssignmentAt(int index) {
		if (!members[index]) {
			throw new IllegalArgumentException();
		}
		
		return assignment[index];
	}

	public void setAssignment(int index, boolean value) {
		if (!members[index]) {
			throw new IllegalArgumentException();
		}

		assignment[index] = value;
	}

	public int getSize() {
		return members.length;
	}

	public boolean agrees(BitvectorSolution solution) {
		boolean agrees = true;
		
		for (int i = 0, n = members.length; i < n; i++) {
			if (members[i] && assignment[i] != solution.get(i)) {
				agrees = false;
			}
		}

		return agrees;
	}

	public void groupConstruction(Unit other) {
		for (int i = 0, n = members.length; i < n; i++) {
			if (other.members[i] && !this.members[i]) {
				this.members[i] = true;
				this.assignment[i] = other.assignment[i];
			}
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(assignment);
		result = prime * result + Arrays.hashCode(members);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Unit other = (Unit) obj;
		if (!Arrays.equals(assignment, other.assignment))
			return false;
		if (!Arrays.equals(members, other.members))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append("(");

		for (int i = 0, n = members.length; i < n; i++) {
			if (members[i]) {
				sb.append("1");
			} else {
				sb.append("0");
			}
		}
		sb.append(", ");
		for (int i = 0, n = members.length; i < n; i++) {
			if (members[i]) {
				if (assignment[i]) {
					sb.append("1");
				} else {
					sb.append("0");
				}

			} else {
				sb.append("*");
			}
		}

		sb.append(")");

		return sb.toString();
	}

}
