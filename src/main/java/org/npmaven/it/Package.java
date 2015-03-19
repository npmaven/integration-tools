package org.npmaven.it;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * Represents a js package delivered from `org.npmaven`.
 */
public class Package {
    public static final String RESOURCE_ROOT = "META-INF/resources/org/npmaven/";

    private final String name;
    private final ClassLoader cl;
    private final Properties props;

    /**
     * Create a js package reference object with the given name and using the default `ClassLoader`.
     * @param name Name of the package, ex `"angular"` or `"d3"`.
     * @throws IOException if hell breaks loose.
     */
    public Package(String name) throws IOException {
        this(name, Package.class.getClassLoader());
    }

    /**
     * Create a js package reference object with the given name and the given `ClassLoader`.
     * @param name Name of the package, ex `"angular"` or `"d3"`.
     * @param cl `ClassLoader` to use for finding the package resources.
     * @throws IOException if hell breaks loose.
     */
    public Package(String name, ClassLoader cl) throws IOException {
        this.name = name;
        this.cl = cl;
        this.props = new Properties();
        props.load(stream("package.properties"));
    }

    /**
     * Gets the name supplied in the constructor.
     * @return the name
     */
    public String getName() {
        return name;
    }

    public String getResourceRoot() {
        return RESOURCE_ROOT + name + '/';
    }

    /**
     * Gets the version of the js package currently in the classpath.
     * @return the version
     */
    public String getVersion() {
        return props.getProperty("version");
    }

    public Asset getMain() {
        return new Asset(this, getName());
    }

    /**
     * Gets the main js name per Bower packaging dressed with the package version for proper browser cache utilization.
     * @return the main js name
     */
    public String getMainBowerName() {
        return getMainBowerPrefixVersioned() + ".js";
    }

    /**
     * Gets an `InputStream` to the main js file per Bower packaging
     * @return the main js file
     */
    public InputStream getMainBowerStream() {
        return stream(getMainBowerNameClean(".js"));
    }

    /**
     * Gets the byte array content from the main js file per Bower packaging
     * @return the main js file
     */
    public byte[] getMainBowerBytes() {
        return bytes(getMainBowerStream());
    }

    /**
     * Gets the String content from the main js file per Bower packaging
     * @return the main js file
     */
    public String getMainBowerString() {
        return bytesToString(getMainBowerBytes());
    }

    /**
     * Gets the minified main js name per Bower packaging dressed with the package version and suffixed with `min` as appropriate.
     * @return the main min js name
     */
    public String getMainBowerNameMin() {
        return getMainBowerPrefixVersioned() + ".min.js";
    }

    /**
     * Gets an `InputStream` to the minified main js file per Bower packaging
     * @return the minified main js file
     */
    public InputStream getMainBowerStreamMin() {
        return stream(getMainBowerNameClean(".min.js")); // TODO
    }

    /**
     * Gets the byte array content from the minified main js file per Bower packaging
     * @return the minified main js file
     */
    public byte[] getMainBowerBytesMin() {
        return bytes(getMainBowerStreamMin());
    }

    /**
     * Gets the String content from the minified main js file per Bower packaging
     * @return the minified main js file
     */
    public String getMainBowerStringMin() {
        return bytesToString(getMainBowerBytesMin());
    }

    /**
     * Gets the main js mapping file name per Bower packaging dressed with the package version and suffixed with `min.js.map` as appropriate.
     * @return the main min js map name
     */
    public String getMainBowerNameMap() {
        return getMainBowerPrefixVersioned() + ".min.js.map";
    }

    /**
     * Gets an `InputStream` to the main js mapping file per Bower packaging
     * @return the main js mapping file
     */
    public InputStream getMainBowerStreamMap() {
        return stream(getMainBowerNameClean(".min.js.map")); // TODO
    }

    /**
     * Gets the byte array content from the main js mapping file per Bower packaging
     * @return the main js mapping file
     */
    public byte[] getMainBowerBytesMap() {
        return bytes(getMainBowerStreamMap());
    }

    /**
     * Gets the String content from the main js mapping file per Bower packaging
     * @return the main js mapping file
     */
    public String getMainBowerStringMap() {
        return bytesToString(getMainBowerBytesMap());
    }

    /**
     * Gets the String content from the main js mapping file per Bower packaging, except with the referenced file
     * names are dressed with the package version.
     * @return the main js mapping file with contents substituted accordingly for versioning
     */
    public String mainBowerStringMapWithVersions() {
        return getMainBowerStringMap(getMainBowerNameMin(), getMainBowerName());
    }

    /**
     * Gets the String content from the main js mapping file per Bower packaging, except with the referenced file
     * names are specified in the arguments
     * @param file the file name to be substituted (this one corresponds to the minified file name)
     * @param src  the src name to be substituted (this one corresponds to the main file name)
     * @return the main js mapping file with contents substituted accordingly
     */
    public String getMainBowerStringMap(String file, String src) {
        String orig = bytesToString(getMainBowerBytesMap());
        String fileReplace = "\"file\":\""+file+"\",";
        String srcReplace =  "\"sources\":[\""+src+"\"],";

        return orig
            .replaceFirst("(?s)\\Q\"file\":\"" + getMainBowerNameClean(".min.js") + "\",\\E", fileReplace)
            .replaceFirst("(?s)\\Q\"sources\":[\""+getMainBowerNameClean(".js")+"\"],\\E", srcReplace);
    }

    private String getMainBowerNameClean(String suffix) {
        String main = props.getProperty("main.bower");

        // Strip leading ./ if there is one
        if(main.startsWith("./")) main = main.substring(2);

        // Strip .js off the end
        main = main.substring(0, main.length()-3);

        main = main+suffix;

        return main;
    }

    private String getMainBowerPrefixVersioned() {
        String main = getMainBowerNameClean("");

        return main + "-" + getVersion();
    }

    private InputStream stream(String rsrc) {
        return cl.getResourceAsStream(getResourceRoot() + rsrc);
    }

    private byte[] bytes(InputStream in) {
        Map<byte[],Integer> bytes = new LinkedHashMap<byte[], Integer>();
        final int SIZE = 1024;
        byte[] buffer = new byte[SIZE];
        int total = 0;

        try {
            for(int read = in.read(buffer); read > 0; read = in.read(buffer)) {
                bytes.put(buffer, read);
                buffer = new byte[SIZE];
                total += read;
            }
        } catch (IOException e) {
            return new byte[0];
        }

        byte[] result = new byte[total];
        int offset = 0;
        for(Map.Entry<byte[], Integer> entry : bytes.entrySet()) {
            byte[] bs = entry.getKey();
            int length = entry.getValue();
            System.arraycopy(bs, 0, result, offset, length);
            offset += length;
        }

        return result;
    }

    private String bytesToString(byte[] bytes) {
        try {
            return new String(bytes, "utf-8");
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }
}
