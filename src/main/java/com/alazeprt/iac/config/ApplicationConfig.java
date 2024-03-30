package com.alazeprt.iac.config;

import com.alazeprt.iac.ui.ProjectUIController;
import com.alazeprt.iac.utils.Project;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

public class ApplicationConfig {
    private static final Logger logger = LogManager.getLogger();
    public static void initialize() {
        ObjectMapper mapper = new ObjectMapper();
        File config = new File(".iac.json");
        if(config.exists()) {
            try {
                Map<String, Object> map = mapper.readValue(config, HashMap.class);
                String version = map.get("version").toString();
            } catch (Exception e) {
                saveDefaultConfig();
            }
        } else {
            saveDefaultConfig();
        }
    }

    public static void initializeContent() {
        logger.info("Getting recents...");
        List<Project> projects = getProjects();
        if(projects.isEmpty()) {
            return;
        }
        for (Project project : projects) {
            ProjectUIController.addProjects(project);
        }
    }

    private static List<Project> getProjects() {
        List<Project> projects = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        File config = new File(".iac.json");
        if(!config.exists()) {
            saveDefaultConfig();
        }
        try {
            Map<String, Object> map = mapper.readValue(config, HashMap.class);
            List<Map<String, Object>> list = (List<Map<String, Object>>) map.get("recents");
            for(Map<String, Object> record : list) {
                projects.add(new Project(record.get("namespace").toString(),
                        Path.of(record.get("path").toString()), UUID.fromString(record.get("uuid").toString())));
            }
        } catch (Exception e) {
        }
        return projects;
    }

    public static List<Project> getProjects(String filter) {
        List<Project> projects = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        File config = new File(".iac.json");
        if(!config.exists()) {
            saveDefaultConfig();
        }
        try {
            Map<String, Object> map = mapper.readValue(config, HashMap.class);
            List<Map<String, Object>> list = (List<Map<String, Object>>) map.get("recents");
            for(Map<String, Object> record : list) {
                if(record.get("namespace").toString().contains(filter) || record.get("path").toString().contains(filter)) {
                    projects.add(new Project(record.get("namespace").toString(),
                            Path.of(record.get("path").toString()), UUID.fromString(record.get("uuid").toString())));
                }
            }
        } catch (Exception e) {
        }
        return projects;
    }

    private static void saveDefaultConfig() {
        logger.warn("config .iac.json not found! Saving default config...");
        ObjectMapper mapper = new ObjectMapper();
        File config = new File(".iac.json");
        try {
            mapper.writeValue(config, Map.of("version", "1.0.0-alpha.1"));
        } catch (IOException e) {
            logger.error("Failed to save default config!", e);
        }
    }

    public static void writeRecentContent(Project... recentProjects) {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> map;
        try {
            map = mapper.readValue(new File(".iac.json"), HashMap.class);
        } catch (IOException e) {
            logger.error("Failed to read .iac.json!", e);
            return;
        }
        List<Project> recentProjectList = Arrays.asList(recentProjects);
        if(map.getOrDefault("recents", "").equals("")) {
            map.put("recents", recentProjectList);
        } else {
            List<Project> configRecentProjectList = (List<Project>) map.get("recents");
            configRecentProjectList.addAll(recentProjectList);
            map.put("recents", configRecentProjectList);
        }
        try {
            mapper.writeValue(new File(".iac.json"), map);
        } catch (IOException e) {
            logger.error("Failed to write .iac.json!", e);
        }
    }

    public static void unwriteRecentContent(UUID uuid) {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> map = null;
        try {
            map = mapper.readValue(new File(".iac.json"), HashMap.class);
        } catch (IOException e) {
            logger.error("Failed to read .iac.json!", e);
            return;
        }
        ((List<LinkedHashMap<String, Object>>) map.get("recents"))
                .removeIf(recentProject -> recentProject.get("uuid").toString().equals(uuid.toString()));
        try {
            mapper.writeValue(new File(".iac.json"), map);
        } catch (IOException e) {
            logger.error("Failed to write .iac.json!", e);
        }
    }

    public static boolean existRecentProject(Project project) {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> map = null;
        try {
            map = mapper.readValue(new File(".iac.json"), HashMap.class);
        } catch (IOException e) {
            logger.error("Failed to read .iac.json!", e);
            return false;
        }
        if(map.get("recents") == null) {
            return false;
        }
        for(LinkedHashMap<String, Object> project1 : ((List<LinkedHashMap<String, Object>>) map.get("recents"))) {
            if(project1.get("uuid").equals(project.getUuid().toString())) {
                return true;
            }
        }
        return false;
    }

    public static void handleRecentContent() {
        // TODO: Change not exists content color -> grey
    }
}
