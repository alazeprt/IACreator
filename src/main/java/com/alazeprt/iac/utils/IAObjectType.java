package com.alazeprt.iac.utils;

public enum IAObjectType {
    ITEM("item", Item.class);
    private final String name;
    private final Class<? extends IAObject> clazz;
    IAObjectType(String name, Class<? extends IAObject> clazz) {
        this.name = name;
        this.clazz = clazz;
    }

    @Override
    public String toString() {
        return name;
    }

    public Class<? extends IAObject> getClazz() {
        return clazz;
    }
}
