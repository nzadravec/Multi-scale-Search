package hr.fer.zemris.zavrad.util;

import java.util.Arrays;
import java.util.Random;

/**
 * This class contains methods for manipulating arrays.
 * 
 * @author Nikola Zadravec
 *
 */
public class ArraysUtil {

	/**
	 * Method copies the boolean array.
	 * 
	 * @param original
	 *            the array to be copied
	 * @return a copy of the original array
	 */
	public static boolean[] copyOf(boolean[] original) {
		return Arrays.copyOf(original, original.length);
	}

	/**
	 * Method copies the int array.
	 * 
	 * @param original
	 *            the array to be copied
	 * @return a copy of the original array
	 */
	public static int[] copyOf(int[] original) {
		return Arrays.copyOf(original, original.length);
	}

	/**
	 * Method fills int array from 0 onwards. For example, if array is length 3,
	 * content will be: 0, 1, 2.
	 * 
	 * @param array
	 *            array to be filled
	 * @return filled array
	 */
	public static int[] linearFillArray(int[] array) {
		for (int i = 0; i < array.length; i++) {
			array[i] = i;
		}

		return array;
	}

	/**
	 * Method rearranges the order of the elements in given array through a
	 * random mechanism.
	 * 
	 * @param array
	 *            array to be rearranged
	 * @param rand
	 *            random number generator
	 * @return rearranged array
	 */
	public static int[] shuffleArray(int[] array, Random rand) {
		for (int i = array.length; i > 1; i--) {
			int b = rand.nextInt(i);
			if (b != i - 1) {
				int e = array[i - 1];
				array[i - 1] = array[b];
				array[b] = e;
			}
		}

		return array;
	}

	/**
	 * Method flips bits of given array through a random mechanism.
	 * 
	 * @param array
	 *            array whose elements is to be flipped
	 * @param rand
	 *            random number generator
	 * @return array with flipped elements
	 */
	public static boolean[] flipBitArray(boolean[] array, Random rand) {
		for (int i = 0; i < array.length; i++) {
			array[i] = rand.nextBoolean();
		}

		return array;
	}

}
