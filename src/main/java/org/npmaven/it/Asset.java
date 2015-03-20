package org.npmaven.it;

/**
 * Represents and provides access to an asset in a javascript package.  Assets can be javascript files or other
 * stuff that tends to get stuffed into the packages.  This class aggregates together the source, the minified
 * source, and the source map of a given javascript asset.  See the `Classifier` enum where we distinguish which
 * edition of the asset you want.
 * <p>
 * A key feature of this wrapping construct is we can do some adjustments on the content from how it was delivered
 * to us.  For instance, we will add the version number to the file names and adjust the minified js and source
 * map to correctly point at these assets.  This approach is important for ensuring correct behavior of asset caching.
 */
public class Asset {
    private final Package parent;
    private final String name;

    Asset(Package parent, String name) {
        this.parent = parent;
        this.name = name;
    }

    /**
     * Gets the name exactly as it was given to us by npm.  This sometimes contains a `./` prefix.
     * @return the raw name
     */
    public String getRawName() {
        return name;
    }

    /**
     * Gets the prefix to be applied to the filenames in this asset which includes the version of the package.
     * For instance, if you have `angular` version `1.3.15`, then this will be `angular-1.3.15`.
     * @return
     */
    public String getVersionedPrefix() {
        String main = getNameClean();

        return main + "-" + parent.getVersion();
    }

    /**
     * Gets the name of the classified asset with the version injected into the name.
     * @param which exactly which edition of this asset you want
     * @return the name of that resource
     */
    public String getVersionedName(Classifier which) {
        return getVersionedPrefix()+getSuffix(which);
    }

    /**
     * Gets the contents of the `DEFAULT` (non minified) file exactly as it was given to us by npm.
     * @return the contents of the non minified asset.
     */
    public String getRaw() {
        return getRaw(Classifier.DEFAULT);
    }

    /**
     * Gets the contents of the file exactly as it was given to us by npm for the given classifier.
     * @param which exactly which edition of the asset you want
     * @return the contents of the asset.
     */
    public String getRaw(Classifier which) {
        return parent.string(getNameClean() + getSuffix(which));
    }

    /**
     * Gets the contents of the file but with the file references adjusted to have the versioned names injected.
     * @param which exactly which edition of the asset you want
     * @return the contents of the asset adjusted as described
     */
    public String getWithAdjustedReferences(Classifier which) {
        return getWithAdjustedReferences(which, "");
    }

    /**
     * Same as `getWithAdjustedReferences(Classifier)` except you can go a step further and add a prefix to the
     * references to allow you to serve them from a different location in your site map.
     * @param which exactly which edition of the asset you want
     * @param prefix the arbitrary string to slap on the front of the references. The sane caller will pass some kind
     *               of path here.
     * @return the contents of the asset adjusted as described
     */
    public String getWithAdjustedReferences(Classifier which, String prefix) {
        String raw = getRaw(which);

        if(raw != null) {
            String src = getVersionedName(Classifier.DEFAULT);
            String srcRegex = "(?s)\\Q\"sources\":[\"" + getNameClean() + getSuffix(Classifier.DEFAULT) + "\"],\\E";
            String srcReplace = "\"sources\":[\"" + prefix + src + "\"],";
            String file = getVersionedName(Classifier.MIN);
            String fileRegex = "(?s)\\Q\"file\":\"" + getNameClean() + getSuffix(Classifier.MIN) + "\",\\E";
            String fileReplace = "\"file\":\"" + prefix + file + "\",";
            String map = getVersionedName(Classifier.MAP);
            String mapRegex = "(?s)\\Q//# sourceMappingURL=" + getNameClean() + getSuffix(Classifier.MAP) + "\\E";
            String mapReplace = "//# sourceMappingURL=" + prefix + map;

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
