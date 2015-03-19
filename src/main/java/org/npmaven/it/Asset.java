package org.npmaven.it;

public class Asset {
    private final Package parent;
    private final String name;

    public Asset(Package parent, String name) {
        this.parent = parent;
        this.name = name;
    }

    public String getRawName() {
        return name;
    }

    public String getVersionedPrefix() {
        String main = getNameClean();

        return main + "-" + parent.getVersion();
    }

    public String getVersionedName(Classifier which) {
        return getVersionedPrefix()+getSuffix(which);
    }

    public String getRaw() {
        return getRaw(Classifier.DEFAULT);
    }

    public String getRaw(Classifier which) {
        return parent.string(getNameClean()+getSuffix(which));
    }

    private String getSuffix(Classifier which) {
        switch (which) {
            case DEFAULT:   return ".js";
            case MIN:       return ".min.js";
            case MAP:       return ".min.js.map";
            default:        return "This unreachable code exists to satisfy a poorly conceived compiler";
        }
    }

    private String getNameClean() {
        String main = name;

        // Strip leading ./ if there is one
        if(main.startsWith("./")) main = main.substring(2);

        // Strip .js off the end
        main = main.substring(0, main.length()-3);

        return main;
    }
}
