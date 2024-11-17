package com.kaba4cow.dependencyinjector.configtools;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

class PropertiesConfigLoader implements ConfigLoader {

	@Override
	public Map<String, Object> loadConfig(String fileName) throws Exception {
		Properties properties = new Properties();
		properties.load(new FileInputStream(fileName));
		Map<String, Object> map = new HashMap<>();
		for (String key : properties.stringPropertyNames())
			map.put(key, properties.getProperty(key));
		return map;
	}

}
