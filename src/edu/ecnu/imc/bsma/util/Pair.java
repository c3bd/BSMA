package edu.ecnu.imc.bsma.util;

import java.util.Comparator;

public class Pair<ValueType> {
	public double _weight;
	public ValueType _value;

	public Pair(double weight, ValueType value) {
		_weight = weight;
		_value = value;
	}

	public static class PairComp implements Comparator<Pair> {
		@Override
		public int compare(Pair arg0, Pair arg1) {
			return Double.compare(arg0._weight, arg1._weight);
		}
	}
}