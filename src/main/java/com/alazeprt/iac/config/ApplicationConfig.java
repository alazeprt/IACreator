package com.alazeprt.iac.config;

import com.alazeprt.iac.ui.ProjectUIController;
import com.alazeprt.iac.utils.Project;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import java.util.Arrays;

public class ApplicationConfig {
    private static final Logger logger = LoggerFactory.getLogger(ApplicationConfig.class.toString());
    public static void initialize() {
        logger.info("Initial application configuration...");
        ObjectMapper mapper = new ObjectMapper();
        File config = new File(".iac.json");
        if(config.exists()) {
            logger.debug("Reading application configuration...");
            try {
                Map<String, Object> map = mapper.readValue(config, HashMap.class);
                String version = map.get("version").toString();
                logger.debug("Application configuration version: " + version);
            } catch (Exception e) {
                logger.debug("Failed to read application configuration: " + e);
                saveDefaultConfig();
            }
        } else {
            saveDefaultConfig();
        }
    }

    public static void initializeContent() {
        List<Project> projects = getProjects();
        if(projects.isEmpty()) {
            logger.warn("Didn't found any recent projects!");
            return;
        }
        for (Project project : projects) {
            ProjectUIController.addProjects(project);
        }
    }

    private static List<Project> getProjects() {
        List<Project> projects = new ArrayList<>();
        logger.info("Getting recent projects...");
        ObjectMapper mapper = new ObjectMapper();
        File config = new File(".iac.json");
        if(!config.exists()) {
            saveDefaultConfig();
        }
        try {
            Map<String, Object> map = mapper.readValue(config, HashMap.class);
            List<Map<String, Object>> list = (List<Map<String, Object>>) map.get("recents");
            for(Map<String, Object> record : list) {
                logger.info("Got project: " + record.get("namespace").toString());
                logger.debug("Project Information: Namespace: " + record.get("namespace").toString() +
                        ", Path: " + record.get("path").toString() +
                        ", UUID: " + record.get("uuid").toString());
                projects.add(new Project(record.get("namespace").toString(),
                        Path.of(record.get("path").toString()), UUID.fromString(record.get("uuid").toString())));
            }
        } catch (Exception e) {
            logger.debug("Failed to get recent projects: " + e);
        }
        return projects;
    }

    public static List<Project> getProjects(String filter) {
        List<Project> projects = new ArrayList<>();
        logger.info("Getting recent projects with filter...");
        ObjectMapper mapper = new ObjectMapper();
        File config = new File(".iac.json");
        if(!config.exists()) {
            saveDefaultConfig();
        }
        try {
            Map<String, Object> map = mapper.readValue(config, HashMap.class);
            List<Map<String, Object>> list = (List<Map<String, Object>>) map.get("recents");
            for(Map<String, Object> record : list) {
                logger.info("Got project: " + record.get("namespace").toString());
                logger.debug("Project Information: Namespace: " + record.get("namespace").toString() +
                        ", Path: " + record.get("path").toString() +
                        ", UUID: " + record.get("uuid").toString());
                if(record.get("namespace").toString().contains(filter) || record.get("path").toString().contains(filter)) {
                    logger.debug("Filter matched: " + record.get("namespace").toString() + " | " + record.get("path").toString());
                    projects.add(new Project(record.get("namespace").toString(),
                            Path.of(record.get("path").toString()), UUID.fromString(record.get("uuid").toString())));
                }
            }
        } catch (Exception e) {
            logger.debug("Failed to get recent projects: " + e);
        }
        return projects;
    }

    private static void saveDefaultConfig() {
        logger.info("Creating default application configuration...");
        ObjectMapper mapper = new ObjectMapper();
        File config = new File(".iac.json");
        try {
            mapper.writeValue(config, Map.of("version", "1.0.0-alpha.1"));
        } catch (IOException e) {
            logger.warn("Failed to create default application configuration: " + e);
            e.printStackTrace();
        }
    }

    public static void writeRecentContent(Project... recentProjects) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> map = mapper.readValue(new File(".iac.json"), HashMap.class);
        List<Project> recentProjectList = Arrays.asList(recentProjects);
        if(map.getOrDefault("recents", "").equals("")) {
            map.put("recents", recentProjectList);
        } else {
            List<Project> configRecentProjectList = (List<Project>) map.get("recents");
            configRecentProjectList.addAll(recentProjectList);
            map.put("recents", configRecentProjectList);
        }
        mapper.writeValue(new File(".iac.json"), map);
    }

    public static void unwriteRecentContent(UUID uuid) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> map = mapper.readValue(new File(".iac.json"), HashMap.class);
        ((List<LinkedHashMap<String, Object>>) map.get("recents"))
                .removeIf(recentProject -> recentProject.get("uuid").equals(uuid));
        mapper.writeValue(new File(".iac.json"), map);
    }

    public static boolean existRecentProject(Project project) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> map = mapper.readValue(new File(".iac.json"), HashMap.class);
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
