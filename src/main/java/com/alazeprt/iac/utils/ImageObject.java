package com.alazeprt.iac.utils;

import java.nio.file.Path;

public abstract class ImageObject extends IAObject {
    private final Path resource;
    public ImageObject(String name, Path resource) {
        super(name);
        this.resource = resource;
    }

    public Path getResource() {
        return resource;
    }
}
