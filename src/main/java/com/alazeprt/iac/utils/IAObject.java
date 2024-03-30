package com.alazeprt.iac.utils;

public abstract class IAObject {
    private final String name;
    public IAObject(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public abstract IAObjectType getType();
    public String toNamespace() {
        return name.toLowerCase().replace(" ", "_");
    }
}
