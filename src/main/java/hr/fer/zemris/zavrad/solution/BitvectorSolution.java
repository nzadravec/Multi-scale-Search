package hr.fer.zemris.zavrad.solution;

import java.util.Arrays;
import java.util.Random;

import hr.fer.zemris.zavrad.util.ArraysUtil;

public class BitvectorSolution extends SingleObjectiveSolution {
	
	private boolean[] bits;
	
	public BitvectorSolution(int size) {
        this.bits = new boolean[size];
    }
	
	public BitvectorSolution(boolean[] bits) {
		this.bits = ArraysUtil.copyOf(bits);
	}
	
	public BitvectorSolution(boolean[] bits, boolean copyArray) {
		if(copyArray) {
			this.bits = ArraysUtil.copyOf(bits);
		} else {
			this.bits = bits;
		}
	}
	
	public BitvectorSolution newLikeThis() {
        return new BitvectorSolution(bits.length);
    }
	
	public BitvectorSolution duplicate() {
        BitvectorSolution duplicate = new BitvectorSolution(bits.length);

        for (int i = 0, n = bits.length; i < n; ++i) {
        	duplicate.bits[i] = this.bits[i];
        }

        return duplicate;
    }
	
	public void randomize(Random rand) {
		for (int i = 0, n = bits.length; i < n; ++i) {
        	bits[i] = rand.nextBoolean();
        }
    }
	
	public boolean[] getBitvector() {
		return bits;
	}
	
	public boolean get(int index) {
        return bits[index];
    }
	
	public void set(int index, boolean value) {
		bits[index] = value;
	}
	
	public void flipBit(Random rand){
		int index = rand.nextInt(bits.length);
        bits[index] ^= true;
    }

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BitvectorSolution other = (BitvectorSolution) obj;
		if (!Arrays.equals(bits, other.bits))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		for (int i = 0, n = bits.length; i < n; ++i) {
        	if(bits[i]) {
        		sb.append("1");
        	} else {
        		sb.append("0");
        	}
        }
		
		return sb.toString();
	}
	
}
