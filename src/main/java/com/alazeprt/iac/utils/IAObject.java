package com.alazeprt.iac.utils;

public abstract class IAObject {
    private String name;
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

    public void setName(String name) {
        this.name = name;
    }
}
