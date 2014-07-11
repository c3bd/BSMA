package edu.ecnu.imc.bsma.generator;

import java.util.List;
import java.util.Random;

import edu.ecnu.imc.bsma.util.Pair;

public class UniformGenerator<ValueType> extends Generator<ValueType> {
	List<Pair<ValueType>> _values;
	Random _random;
	ValueType _lastvalue;

	public UniformGenerator(List<Pair<ValueType>> values) {
		_values = values;
		_random = new Random();
		_lastvalue = null;
	}

	@Override
	public ValueType nextString() {
		int idx = Math.abs(_random.nextInt() % _values.size());
		return _values.get(idx)._value;
	}

	@Override
	public ValueType lastString() {
		// TODO Auto-generated method stub
		return null;
	}
}
