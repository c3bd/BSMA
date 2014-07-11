package edu.ecnu.imc.bsma.workloads;

import java.util.List;

import edu.ecnu.imc.bsma.db.DB;
import edu.ecnu.imc.bsma.generator.Generator;

public abstract class QueryWrapper {
	protected List<Generator> gens;

	public QueryWrapper(List<Generator> gens) {
		this.gens = gens;
	}

	public abstract void doAnalysisQuery(DB db);
}
