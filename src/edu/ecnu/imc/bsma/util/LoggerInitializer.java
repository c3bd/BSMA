package edu.ecnu.imc.bsma.util;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.log4j.PropertyConfigurator;

/**
 * 本文件只是用来初始化log4j的
 * @author xiafan
 *
 */
public class LoggerInitializer {
	static {
		if (System.getProperty("log4j.defaultInitOverride", "true")
				.equalsIgnoreCase("true")) {
			String config = System.getProperty("log4j.configuration",
					"log4j-server.properties");
			URL configLocation = null;
			try {
				// try loading from a physical location first.
				configLocation = new URL(config);
			} catch (MalformedURLException ex) {

			}
			// Now convert URL to a filename
			String configFileName = null;
			try {
				// first try URL.getFile() which works for opaque URLs
				// (file:foo) and paths without spaces
				if (configLocation != null)
					configFileName = configLocation.getFile();
				// File configFile = new File(configFileName);
				File configFile = new File("./conf").getAbsoluteFile();
				configFile = new File(configFile, "log4j-server.properties");
				System.out.println("configFile:" + configFile);
				// then try alternative approach which works for all
				// hierarchical URLs with or without spaces
				if (!configFile.exists())
					configFileName = new File(configLocation.toURI())
							.getCanonicalPath();
				else
					configFileName = configFile.getAbsolutePath();
			} catch (Exception e) {
				throw new RuntimeException(
						"Couldn't convert log4j configuration location to a valid file",
						e);
			}
			PropertyConfigurator.configureAndWatch(configFileName, 10000);
			org.apache.log4j.Logger.getLogger(Config.class).info(
					"Logging initialized");
		}
	}

	public LoggerInitializer() {

	}
}
