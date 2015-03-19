package org.npmaven.it;

public class Asset {
    private final Package parent;
    private final String name;

    public Asset(Package parent, String name) {
        this.parent = parent;
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
