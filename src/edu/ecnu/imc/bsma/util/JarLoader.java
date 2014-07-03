package edu.ecnu.imc.bsma.util;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;

import edu.ecnu.imc.bsma.db.DBFactory;

/**
 * load a batch of jar file and return a instance of the specified class
 * 
 * @author xiafan
 * 
 */
public class JarLoader {
	public static Class loadClass(String jarDir, List<String> jars,
			String className) throws ClassNotFoundException,
			MalformedURLException {
		URL[] urls = new URL[jars.size()];
		for (int i = 0; i < jars.size(); i++) {
			File file = new File(jarDir, jars.get(i));
			urls[i] = file.toURI().toURL();
		}
		URLClassLoader loader = new URLClassLoader(urls);
		// Class clazz = loader.loadClass(className);//
		// "oracle.jdbc.driver.OracleDriver"
		return loader.loadClass(className);
	}

	public static DBFactory loadFactory(String jarDir, List<String> jars,
			String className) throws Exception {
		DBFactory factory = (DBFactory) loadClass(jarDir, jars, className)
				.newInstance();
		return factory;
	}
}
