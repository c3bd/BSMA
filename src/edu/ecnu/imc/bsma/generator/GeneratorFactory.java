package edu.ecnu.imc.bsma.generator;

import java.util.List;

import edu.ecnu.imc.bsma.util.Pair;

public class GeneratorFactory {
	public static Generator create(String type, List<Pair> seeds) {
		Generator<List<String>> ret = null;
		if (type.equals("uniform")) {
			ret = new UniformGenerator(seeds);
		} else {
			ret = new DiscreteGenerator(seeds);
		}
		return ret;
	}
}
