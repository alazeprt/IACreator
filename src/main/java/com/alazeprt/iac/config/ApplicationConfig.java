package com.alazeprt.iac.config;

import com.alazeprt.iac.ui.ProjectUIController;
import com.alazeprt.iac.utils.Project;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import java.util.Arrays;

public class ApplicationConfig {
    public static void initialize() {
        ObjectMapper mapper = new ObjectMapper();
        File config = new File("iac.json");
        if(config.exists()) {
            try {
                Map<String, Object> map = mapper.readValue(config, HashMap.class);
                List<Map<String, Object>> list = (List<Map<String, Object>>) map.get("recents");
                for(Map<String, Object> record : list) {
                    Project project = new Project(record.get("namespace").toString(),
                            Path.of(record.get("path").toString()), UUID.fromString(record.get("uuid").toString()));
                }
            } catch (Exception e) {
                System.out.println(e);
                saveDefaultConfig();
            }
        } else {
            saveDefaultConfig();
        }
    }

    public static void initializeContent() {
        ObjectMapper mapper = new ObjectMapper();
        File config = new File("iac.json");
        try {
            Map<String, Object> map = mapper.readValue(config, HashMap.class);
            List<Map<String, Object>> list = (List<Map<String, Object>>) map.get("recents");
            for(Map<String, Object> record : list) {
                System.out.println(record.get("namespace").toString());
                Project project = new Project(record.get("namespace").toString(),
                        Path.of(record.get("path").toString()), UUID.fromString(record.get("uuid").toString()));
                ProjectUIController.addRecentProjects(project);
            }
        } catch (Exception e) {
            System.out.println(e);
            saveDefaultConfig();
        }
    }

    public static void saveDefaultConfig() {
        ObjectMapper mapper = new ObjectMapper();
        File config = new File("iac.json");
        try {
            mapper.writeValue(config, Map.of("version", "1.0.0-alpha.1"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeRecentContent(Project... recentProjects) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> map = mapper.readValue(new File("iac.json"), HashMap.class);
        List<Project> recentProjectList = Arrays.asList(recentProjects);
        if(map.getOrDefault("recents", "").equals("")) {
            map.put("recents", recentProjectList);
        } else {
            List<Project> configRecentProjectList = (List<Project>) map.get("recents");
            configRecentProjectList.addAll(recentProjectList);
            map.put("recents", configRecentProjectList);
        }
        mapper.writeValue(new File("iac.json"), map);
    }

    public static void unwriteRecentContent(UUID uuid) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> map = mapper.readValue(new File("iac.json"), HashMap.class);
        ((List<LinkedHashMap<String, Object>>) map.get("recents"))
                .removeIf(recentProject -> recentProject.get("uuid").equals(uuid));
        mapper.writeValue(new File("iac.json"), map);
    }

    public static boolean existRecentProject(Project project) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> map = mapper.readValue(new File("iac.json"), HashMap.class);
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
