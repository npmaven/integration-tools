package org.npmaven.it;

public class Asset {
    private final Package parent;
    private final String name;
    private final Classifier classifier;

    public Asset(Package parent, String name, Classifier classifier) {
        this.parent = parent;
        this.name = name;
        this.classifier = classifier;
    }

    public String getName() {
        return name;
    }
}
