package com.alazeprt.iac.config;

import com.alazeprt.iac.utils.IAObject;
import com.alazeprt.iac.utils.Item;
import com.alazeprt.iac.utils.RecentProject;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

public class ProjectConfig {
    private static final Logger logger = LogManager.getLogger();
    public static void create(RecentProject recentProject) {
        logger.info("Creating project config...");
        File file = new File(recentProject.getPath() + "/.iac.json");
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> root = new HashMap<>();
        Map<String, Object> iacInfo = new HashMap<>();
        iacInfo.put("uuid", recentProject.getUuid());
        iacInfo.put("namespace", recentProject.getNamespace());
        root.put("iacinfo", iacInfo);
        if(file.exists()) {
            try {
                Map<String, Object> map = mapper.readValue(file, HashMap.class);
                if(((Map<String, Object>) map.get("iacinfo")).get("uuid").equals(recentProject.getUuid())) {
                    return;
                }
            } catch (Exception ignored) {}
            file.delete();
            try {
                mapper.writeValue(file, root);
            } catch (IOException ex) {
                logger.error("Failed to write .iac.json!", ex);
            }
        }
        file.delete();
        try {
            mapper.writeValue(file, root);
        } catch (IOException ex) {
            logger.error("Failed to write .iac.json!", ex);
        }
    }

    public static RecentProject getProject(String path) {
        File file = new File(path + "/.iac.json");
        ObjectMapper mapper = new ObjectMapper();
        if(file.exists()) {
            try {
                Map<String, Object> map = mapper.readValue(file, HashMap.class);
                Map<String, Object> iacInfo = (Map<String, Object>) map.get("iacinfo");
                return new RecentProject(iacInfo.get("namespace").toString(), Path.of(path),
                        UUID.fromString(iacInfo.get("uuid").toString()));
            } catch (Exception ignored) { }
        }
        return null;
    }

    public static void writeProjectObject(IAObject iaObject, IAConfig config) {
        File file = new File(config.root() + "/.iac.json");
        ObjectMapper mapper = new ObjectMapper();
        if(!file.exists()) {
            config.generateDefaultConfig();
        }
        Map<String, Object> map;
        try {
            map = mapper.readValue(file, HashMap.class);
            if(map == null) throw new RuntimeException("Map is null!");
        } catch (Exception e) {
            logger.error("Failed to read project's config!", e);
            map = new HashMap<>();
        }
        Map<String, Object> objects;
        try {
            objects = (Map<String, Object>) map.get("objects");
            if(objects == null) throw new RuntimeException("Objects is null!");
        } catch (Exception e) {
            logger.error("Failed to read objects of project's config!", e);
            objects = new HashMap<>();
        }
        Map<String, String> itemMap = new HashMap<>();
        itemMap.put("type", iaObject.getType().name());
        itemMap.put("display_name", iaObject.getName());
        if(iaObject instanceof Item) {
            itemMap.put("resource", ((Item) iaObject).getResource().toString());
        }
        objects.put(iaObject.toNamespace(), itemMap);
        map.put("objects", objects);
        try {
            mapper.writeValue(file, map);
        } catch (IOException e) {
            logger.error("Failed to write project's config!", e);
        }
    }

    public static List<IAObject> readProjectObject(IAConfig config) {
        File file = new File(config.root() + "/.iac.json");
        ObjectMapper mapper = new ObjectMapper();
        if(!file.exists()) {
            config.generateDefaultConfig();
            return new ArrayList<>();
        }
        Map<String, Object> map;
        try {
            map = mapper.readValue(file, HashMap.class);
            if(map == null) throw new RuntimeException("Map is null!");
        } catch (Exception e) {
            logger.error("Failed to read project's config!", e);
            return new ArrayList<>();
        }
        Map<String, Object> objects;
        try {
            objects = (Map<String, Object>) map.get("objects");
            if(objects == null) throw new RuntimeException("Objects is null!");
        } catch (Exception e) {
            logger.error("Failed to read objects of project's config!", e);
            return new ArrayList<>();
        }
        List<IAObject> iaObjects = new ArrayList<>();
        for(Map.Entry<String, Object> entry : objects.entrySet()) {
            Map<String, String> itemMap = (Map<String, String>) entry.getValue();
            String type = itemMap.get("type");
            String name = itemMap.get("display_name");
            if(type.equalsIgnoreCase("item")) {
                iaObjects.add(new Item(name, Path.of(itemMap.get("resource"))));
            }
        }
        return iaObjects;
    }

    public static void removeProjectObject(Item item, IAConfig iaConfig) {
        File file = new File(iaConfig.root() + "/.iac.json");
        ObjectMapper mapper = new ObjectMapper();
        if(!file.exists()) {
            iaConfig.generateDefaultConfig();
            return;
        }
        Map<String, Object> map;
        try {
            map = mapper.readValue(file, HashMap.class);
            if(map == null) throw new RuntimeException("Map is null!");
        } catch (Exception e) {
            logger.error("Failed to read project's config!", e);
            return;
        }
        Map<String, Object> objects;
        try {
            objects = (Map<String, Object>) map.get("objects");
            if(objects == null) throw new RuntimeException("Objects is null!");
        } catch (Exception e) {
            logger.error("Failed to read objects of project's config!", e);
            return;
        }
        objects.remove(item.toNamespace());
        map.put("objects", objects);
        try {
            mapper.writeValue(file, map);
        } catch (IOException e) {
            logger.error("Failed to write project's config!", e);
        }
    }
}
