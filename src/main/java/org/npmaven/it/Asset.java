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
        return parent.string(getNameClean() + getSuffix(which));
    }

    public String getWithAdjustedReferences(Classifier which) {
        String src  = getVersionedName(Classifier.DEFAULT);
        String file = getVersionedName(Classifier.MIN);
        String map  = getVersionedName(Classifier.MAP);
        String srcReplace  = "\"sources\":[\""+src+"\"],";
        String fileReplace = "\"file\":\""+file+"\",";
        String mapReplace  = "//# sourceMappingURL="+map;

        return getRaw(which)
                .replaceFirst("(?s)\\Q\"sources\":[\""       + getNameClean() + getSuffix(Classifier.DEFAULT) + "\"],\\E", srcReplace)  // for source map json file
                .replaceFirst("(?s)\\Q\"file\":\""           + getNameClean() + getSuffix(Classifier.MIN)     + "\",\\E",  fileReplace) // for source map json file
                .replaceFirst("(?s)\\Q//# sourceMappingURL=" + getNameClean() + getSuffix(Classifier.MAP)     + "\\E",     mapReplace); // for minified js file
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
