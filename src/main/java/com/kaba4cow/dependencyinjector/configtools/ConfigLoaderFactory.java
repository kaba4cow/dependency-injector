package com.kaba4cow.dependencyinjector.configtools;

import java.util.Objects;

/**
 * Factory class responsible for creating and loading the configuration loader based on the given configuration file.
 */
public class ConfigLoaderFactory {

	private ConfigLoaderFactory() {}

	/**
	 * Returns a configuration loader based on the file type or format.
	 *
	 * @param configFile the configuration file path
	 * 
	 * @return the configuration loader
	 * 
	 * @throws Exception if an error occurs while loading the config
	 */
	public static ConfigLoader getConfigLoader(String fileName) {
		Objects.requireNonNull(fileName);
		String extension = fileName.substring(fileName.lastIndexOf('.') + 1);
		if (extension.equalsIgnoreCase("properties"))
			return new PropertiesConfigLoader();
		else if (extension.equalsIgnoreCase("json"))
			return new JSONConfigLoader();
		else if (extension.equalsIgnoreCase("yaml") || extension.equalsIgnoreCase("yml"))
			return new YAMLConfigLoader();
		else
			throw new IllegalArgumentException(String.format("Unsupported config file extension %s", extension));
	}

}
