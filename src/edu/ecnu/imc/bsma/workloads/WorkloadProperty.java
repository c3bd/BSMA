package edu.ecnu.imc.bsma.workloads;

import java.util.HashMap;
import java.util.Properties;

public class WorkloadProperty {
	private HashMap<String, Properties> propMap = new HashMap<String, Properties>();

	public Properties getDefaultProps(String query) {
		return propMap.get(query);
	}
}
