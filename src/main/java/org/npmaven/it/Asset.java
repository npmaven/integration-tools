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
        String raw = getRaw(which);

        if(raw != null) {
            String src = getVersionedName(Classifier.DEFAULT);
            String srcRegex = "(?s)\\Q\"sources\":[\"" + getNameClean() + getSuffix(Classifier.DEFAULT) + "\"],\\E";
            String srcReplace = "\"sources\":[\"" + src + "\"],";
            String file = getVersionedName(Classifier.MIN);
            String fileRegex = "(?s)\\Q\"file\":\"" + getNameClean() + getSuffix(Classifier.MIN) + "\",\\E";
            String fileReplace = "\"file\":\"" + file + "\",";
            String map = getVersionedName(Classifier.MAP);
            String mapRegex = "(?s)\\Q//# sourceMappingURL=" + getNameClean() + getSuffix(Classifier.MAP) + "\\E";
            String mapReplace = "//# sourceMappingURL=" + map;

            return raw
                    .replaceFirst(srcRegex, srcReplace)  // for source map json file
                    .replaceFirst(fileRegex, fileReplace) // for source map json file
                    .replaceFirst(mapRegex, mapReplace); // for minified js file
        }
        else {
            return null;
        }
    }

    private String getSuffix(Classifier which) {
        switch (which) {
            case DEFAULT:   return ".js";
            case MIN:       return ".min.js";
            case MAP:       return ".min.js.map";
            default:        assert false: "You added an enum in Classifier and didn't update me"; return null;
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
