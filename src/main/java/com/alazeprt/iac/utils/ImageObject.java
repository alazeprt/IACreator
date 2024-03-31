package com.alazeprt.iac.utils;

import java.nio.file.Path;

public abstract class ImageObject extends IAObject {
    private Path resource;
    public ImageObject(String name, Path resource) {
        super(name);
        this.resource = resource;
    }

    public Path getResource() {
        return resource;
    }

    public void setResource(Path resource) {
        this.resource = resource;
    }
}
