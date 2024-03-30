package com.alazeprt.iac.utils;

public enum IAObjectType {
    ITEM("item", Item.class);
    private final String name;
    private final Class<?> clazz;
    IAObjectType(String name, Class<?> clazz) {
        this.name = name;
        this.clazz = clazz;
    }

    @Override
    public String toString() {
        return name;
    }

    public Class<?> getClazz() {
        return clazz;
    }
}
