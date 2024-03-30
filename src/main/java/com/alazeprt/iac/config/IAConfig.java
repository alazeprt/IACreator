package com.alazeprt.iac.config;

import com.fasterxml.jackson.databind.cfg.ConfigFeature;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class IAConfig {
    private final File root;
    private final String projectName;
    private static final Logger logger = LogManager.getLogger();
    public IAConfig(String projectName, File root) {
        this.root = root;
        this.projectName = projectName;
    }

    public void generateDefaultConfig() {
        if(defaultConfigExists()) {
            return;
        }
        Map<String, Object> map = new HashMap<>();
        Map<String, String> info = new HashMap<>();
        info.put("namespace", projectName);
        map.put("info", info);
        YAMLMapper mapper = new YAMLMapper();
        mapper.disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER);
        try {
            File file = new File(root + "/configs/");
            if(!file.exists()) {
                if(!file.mkdirs()) {
                    logger.error("Failed to create configs folder!");
                    return;
                }
            }
            mapper.writeValue(new File(root + "/configs/" + projectName + ".yml"), map);
            logger.info("Generated project's default config");
        } catch (IOException e) {
            logger.error("Failed to generate project's default config!", e);
        }
    }

    private boolean defaultConfigExists() {
        YAMLMapper mapper = new YAMLMapper();
        mapper.disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER);
        try {
            Map<String, Object> map = mapper.readValue(new File(root + "/configs/" + projectName + ".yml"), HashMap.class);
            Map<String, Object> info = (Map<String, Object>) map.get("info");
            String namespace = info.get("namespace").toString();
            if(namespace.equals(projectName)) {
                return true;
            } else {
                logger.warn("Project's default config's namespace is wrong!");
                return false;
            }
        } catch (IOException e) {
            logger.warn("Failed to read project's default config!", e);
            return false;
        }
    }
}
