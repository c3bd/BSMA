package edu.ecnu.imc.bsma.util;

import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Config {
	private static final Logger logger = LoggerFactory.getLogger(Config.class);
	public static Config instance = new Config();

	Properties prop = new Properties();

	public Config() {

	}

	public void reConfig() {
	}

	public void init() throws IOException {
		String os = System.getProperty("os.name").toLowerCase();
		System.out.println("the os is " + os);
		String configFile = "./server.properties";
		if (os.contains("window")) {
			configFile = "./win_server.properties";
		} else if (os.contains("mac")) {
			configFile = "./mac_server.properties";
		}
		init(configFile);
	}

	int id = 0;

	public void setProp(String key, String value) {
		prop.setProperty(key, value);
	}

	public String getProp(String key, String defValue) {
		return prop.getProperty(key, defValue);
	}

	public Properties getProps() {
		return prop;
	}

	public void setID(int id) {
		this.id = id;
	}

	public int getID() {
		return id;
	}

	public String getJarDir() {
		return prop.getProperty("jardir", "./jardir");
	}

	public void initTest() throws IOException {
		String os = System.getProperty("os.name").toLowerCase();
		System.out.println("the os is " + os);
		String configFile = "./server_test.properties";
		if (os.contains("window")) {
			configFile = "./win_server_test.properties";
		} else if (os.contains("mac")) {
			configFile = "./mac_server.properties";
		}
		init(configFile);
	}

	public void init(String configFile) throws IOException {
		prop.load(getClass().getClassLoader().getResourceAsStream(configFile));
		logger.info("config file :" + configFile + " config: " + prop);
	}

	public static void main(String[] args) throws IOException {
		Config.instance.init();
	}

}
