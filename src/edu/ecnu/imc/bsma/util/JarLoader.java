package edu.ecnu.imc.bsma.util;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;

import edu.ecnu.imc.bsma.db.DBFactory;

/**
 * load a batch of jar file and return a instance of the specified class
 * 
 * @author xiafan
 * 
 */
public class JarLoader {
	public static Object load(String[] jars, String className) throws Exception {
		File file = new File(className);
		URLClassLoader loader = new URLClassLoader(new URL[] { file.toURI()
				.toURL() });
		Class clazz = loader.loadClass(className);// "oracle.jdbc.driver.OracleDriver"
		return clazz.newInstance();
	}

	public static DBFactory loadFactory(String[] jars, String className)
			throws Exception {
		DBFactory factory = (DBFactory) load(jars, className);
		return factory;
	}
}
