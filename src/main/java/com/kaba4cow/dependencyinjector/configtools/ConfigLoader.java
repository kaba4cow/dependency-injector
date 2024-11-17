package com.kaba4cow.dependencyinjector.configtools;

import java.util.Map;

/**
 * Interface that defines the methods for loading configuration data from a file.
 */
public interface ConfigLoader {

	/**
	 * Loads configuration from the specified file.
	 *
	 * @param configFile the path to the configuration file
	 * 
	 * @return a map containing the configuration data
	 * 
	 * @throws Exception if an error occurs during loading
	 */
	public Map<String, Object> loadConfig(String fileName) throws Exception;

}
