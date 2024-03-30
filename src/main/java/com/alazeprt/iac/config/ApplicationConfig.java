package com.alazeprt.iac.config;

import com.alazeprt.iac.ui.CreateProjectController;
import com.alazeprt.iac.utils.RecentProject;
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
        logger.info("Getting recent projects...");
        List<RecentProject> recentProjects = getProjects();
        if(recentProjects.isEmpty()) {
            return;
        }
        for (RecentProject recentProject : recentProjects) {
            CreateProjectController.addProjects(recentProject);
        }
    }

    private static List<RecentProject> getProjects() {
        List<RecentProject> recentProjects = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        File config = new File(".iac.json");
        if(!config.exists()) {
            saveDefaultConfig();
        }
        try {
            Map<String, Object> map = mapper.readValue(config, HashMap.class);
            List<Map<String, Object>> list = (List<Map<String, Object>>) map.get("recents");
            for(Map<String, Object> record : list) {
                recentProjects.add(new RecentProject(record.get("namespace").toString(),
                        Path.of(record.get("path").toString()), UUID.fromString(record.get("uuid").toString())));
            }
        } catch (Exception e) {
        }
        return recentProjects;
    }

    public static List<RecentProject> getProjects(String filter) {
        List<RecentProject> recentProjects = new ArrayList<>();
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
                    recentProjects.add(new RecentProject(record.get("namespace").toString(),
                            Path.of(record.get("path").toString()), UUID.fromString(record.get("uuid").toString())));
                }
            }
        } catch (Exception e) {
        }
        return recentProjects;
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

    public static void writeRecentContent(RecentProject... recentProjects) {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> map;
        try {
            map = mapper.readValue(new File(".iac.json"), HashMap.class);
        } catch (IOException e) {
            logger.error("Failed to read .iac.json!", e);
            return;
        }
        List<RecentProject> recentRecentListProject = Arrays.asList(recentProjects);
        List<RecentProject> recentProjectList = getProjects();
        List<Map<String, Object>> projectMaps = new ArrayList<>();
        recentProjectList.forEach(project -> {
            Map<String, Object> projectMap = new HashMap<>();
            projectMap.put("namespace", project.getNamespace());
            projectMap.put("path", project.getPath().toString());
            projectMap.put("uuid", project.getUuid().toString());
            projectMaps.add(projectMap);
        });
        recentRecentListProject.forEach(project -> {
            Map<String, Object> projectMap = new HashMap<>();
            projectMap.put("namespace", project.getNamespace());
            projectMap.put("path", project.getPath().toString());
            projectMap.put("uuid", project.getUuid().toString());
            projectMaps.add(projectMap);
        });
        map.put("recents", projectMaps);
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

    public static boolean existRecentProject(RecentProject recentProject) {
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
            if(project1.get("uuid").equals(recentProject.getUuid().toString())) {
                return true;
            }
        }
        return false;
    }

    public static void handleRecentContent() {
        // TODO: Change not exists content color -> grey
    }
}
