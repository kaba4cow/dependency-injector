package com.kaba4cow.dependencyinjector.configtools;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;

class YAMLConfigLoader implements ConfigLoader {

	@Override
	public Map<String, Object> loadConfig(String fileName) throws Exception {
		Yaml yaml = new Yaml();
		Map<String, Object> data = yaml.load(Files.newBufferedReader(Paths.get(fileName)));
		Map<String, Object> map = new HashMap<>();
		readYAMLObject(data, map, "");
		return map;
	}

	@SuppressWarnings("unchecked")
	private void readYAMLObject(Map<String, Object> yamlData, Map<String, Object> map, String parentPath) {
		for (Map.Entry<String, Object> entry : yamlData.entrySet()) {
			String path = path(parentPath, entry.getKey());
			Object value = entry.getValue();
			if (value instanceof Map)
				readYAMLObject((Map<String, Object>) value, map, path);
			else if (value instanceof List)
				map.put(path, value);
			else
				map.put(path, value);
		}
	}

	private String path(String path, String key) {
		return path.isEmpty() ? key : (path + "." + key);
	}

}
