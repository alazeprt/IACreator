package com.alazeprt.iac.config;

import com.alazeprt.iac.utils.IAObject;
import com.alazeprt.iac.utils.Item;
import com.fasterxml.jackson.databind.cfg.ConfigFeature;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

    public void writeItemConfig(Item item) {
        YAMLMapper mapper = new YAMLMapper();
        mapper.disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER);
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> items = new HashMap<>();
        try {
            map = mapper.readValue(new File(root + "/configs/" + projectName + ".yml"), HashMap.class);
            items = (Map<String, Object>) map.getOrDefault("items", new HashMap<>());
        } catch (IOException e) {
            logger.error("Failed to write item's config!", e);
        }
        Map<String, Object> itemMap = new HashMap<>();
        itemMap.put("display_name", item.getName());
        itemMap.put("permission", item.toNamespace());
        Map<String, Object> resourceMap = new HashMap<>();
        resourceMap.put("generate", "true");
        resourceMap.put("textures", List.of("item/" + item.toNamespace() + ".png"));
        itemMap.put("resource", resourceMap);
        items.put(item.toNamespace(), itemMap);
        map.put("items", items);
        try {
            mapper.writeValue(new File(root + "/configs/" + projectName + ".yml"), map);
        } catch (IOException e) {
            logger.error("Failed to write item's config!", e);
        }
    }

    public File getRoot() {
        return root;
    }

    public String getProjectName() {
        return projectName;
    }

    public void removeItemConfig(Item item) {
        YAMLMapper mapper = new YAMLMapper();
        mapper.disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER);
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> items = new HashMap<>();
        try {
            map = mapper.readValue(new File(root + "/configs/" + projectName + ".yml"), HashMap.class);
            items = (Map<String, Object>) map.getOrDefault("items", new HashMap<>());
        } catch (IOException e) {
            logger.error("Failed to write item's config!", e);
        }
        items.remove(item.toNamespace());
        map.put("items", items);
        try {
            mapper.writeValue(new File(root + "/configs/" + projectName + ".yml"), map);
        } catch (IOException e) {
            logger.error("Failed to write item's config!", e);
        }
    }
}
