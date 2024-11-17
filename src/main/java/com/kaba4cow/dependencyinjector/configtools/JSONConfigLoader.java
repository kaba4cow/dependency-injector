package com.kaba4cow.dependencyinjector.configtools;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

class JSONConfigLoader implements ConfigLoader {

	@Override
	public Map<String, Object> loadConfig(String fileName) throws Exception {
		JSONObject json = new JSONObject(new String(Files.readAllBytes(Paths.get(fileName))));
		Map<String, Object> map = new HashMap<>();
		readJSONObject(json, map, "");
		return map;
	}

	private void readJSONObject(JSONObject json, Map<String, Object> map, String parentPath) {
		for (String key : json.keySet()) {
			String path = path(parentPath, key);
			Object value = json.get(key);
			if (value instanceof JSONObject)
				readJSONObject(json.getJSONObject(key), map, path);
			else if (value instanceof JSONArray)
				map.put(path, json.getJSONArray(key).toList());
			else
				map.put(path, value);
		}
	}

	private String path(String path, String key) {
		return path.isEmpty() ? key : (path + "." + key);
	}

}
