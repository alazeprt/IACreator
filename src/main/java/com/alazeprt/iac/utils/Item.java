package com.alazeprt.iac.utils;

import java.nio.file.Path;

public class Item extends ImageObject {
    public Item(String name, Path resource) {
        super(name, resource);
    }

    @Override
    public IAObjectType getType() {
        return IAObjectType.ITEM;
    }
}
