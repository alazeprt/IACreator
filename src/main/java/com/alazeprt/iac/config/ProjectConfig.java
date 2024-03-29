package com.alazeprt.iac.config;

import com.alazeprt.iac.utils.Project;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ProjectConfig {
    public static void create(Project project) {
        File file = new File(project.getPath() + "/.iac.json");
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> root = new HashMap<>();
        Map<String, Object> iacInfo = new HashMap<>();
        iacInfo.put("uuid", project.getUuid());
        iacInfo.put("namespace", project.getNamespace());
        root.put("iacinfo", iacInfo);
        if(file.exists()) {
            try {
                Map<String, Object> map = mapper.readValue(file, HashMap.class);
                if(((Map<String, Object>) map.get("iacinfo")).get("uuid").equals(project.getUuid())) {
                    return;
                }
            } catch (Exception ignored) {}
            file.delete();
            try {
                mapper.writeValue(file, root);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
        file.delete();
        try {
            mapper.writeValue(file, root);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static Project getProject(String path) {
        File file = new File(path + "/.iac.json");
        ObjectMapper mapper = new ObjectMapper();
        if(file.exists()) {
            try {
                Map<String, Object> map = mapper.readValue(file, HashMap.class);
                Map<String, Object> iacInfo = (Map<String, Object>) map.get("iacinfo");
                return new Project(iacInfo.get("namespace").toString(), Path.of(path),
                        UUID.fromString(iacInfo.get("uuid").toString()));
            } catch (Exception ignored) { }
        }
        return null;
    }
}
