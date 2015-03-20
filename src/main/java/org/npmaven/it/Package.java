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

    public Asset getBowerMain() {
        return new Asset(this, props.getProperty("bower.main"));
    }

    public Asset getNpmMain() {
        return new Asset(this, props.getProperty("main"));
    }

    String string(String rsrc) {
        return bytesToString(bytes(stream(rsrc)));
    }

    private InputStream stream(String rsrc) {
        return cl.getResourceAsStream(getResourceRoot() + rsrc);
    }

    private byte[] bytes(InputStream in) {
        if(in != null) {
            Map<byte[], Integer> bytes = new LinkedHashMap<byte[], Integer>();
            final int SIZE = 1024;
            byte[] buffer = new byte[SIZE];
            int total = 0;

            try {
                for (int read = in.read(buffer); read > 0; read = in.read(buffer)) {
                    bytes.put(buffer, read);
                    buffer = new byte[SIZE];
                    total += read;
                }
            } catch (IOException e) {
                return new byte[0];
            }

            byte[] result = new byte[total];
            int offset = 0;
            for (Map.Entry<byte[], Integer> entry : bytes.entrySet()) {
                byte[] bs = entry.getKey();
                int length = entry.getValue();
                System.arraycopy(bs, 0, result, offset, length);
                offset += length;
            }

            return result;
        }
        else {
            return null;
        }
    }

    private String bytesToString(byte[] bytes) {
        if(bytes != null) {
            try {
                return new String(bytes, "utf-8");
            } catch (UnsupportedEncodingException e) {
                return null;
            }
        }
        else {
            return null;
        }
    }
}
