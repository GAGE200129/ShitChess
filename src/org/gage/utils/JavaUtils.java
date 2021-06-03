package org.gage.utils;

import java.util.List;

public class JavaUtils {

	public static float[] FloatArrayListToFloatArray(List<Float> arr) {
		float[] result = new float[arr.size()];
		for (int i = 0; i < arr.size(); i++) {
			result[i] = arr.get(i);
		}

		return result;
	}

	public static int[] IntArrayListToIntArray(List<Integer> arr) {
		int[] result = new int[arr.size()];
		for (int i = 0; i < arr.size(); i++) {
			result[i] = arr.get(i);
		}

		return result;
	}

}
