package org.npmaven.it;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.regex.Pattern;

public class Package {
    private final String name;
    private final ClassLoader cl;
    private final Properties props;

    public Package(String name) throws IOException {
        this(name, Package.class.getClassLoader());
    }
    public Package(String name, ClassLoader cl) throws IOException {
        this.name = name;
        this.cl = cl;
        this.props = new Properties();
        props.load(stream("package.properties"));
    }

    public String getName() {
        return name;
    }

    public String getVersion() {
        return props.getProperty("version");
    }

    public String getMainBowerName() {
        return getMainBowerPrefixVersioned() + ".js";
    }

    public InputStream getMainBowerStream() {
        return stream(getMainBowerNameClean(".js"));
    }

    public byte[] getMainBowerBytes() {
        return bytes(getMainBowerStream());
    }

    public String getMainBowerString() {
        return bytesToString(getMainBowerBytes());
    }

    public String getMainBowerNameMin() {
        return getMainBowerPrefixVersioned() + ".min.js";
    }

    public InputStream getMainBowerStreamMin() {
        return stream(getMainBowerNameClean(".min.js")); // TODO
    }

    public byte[] getMainBowerBytesMin() {
        return bytes(getMainBowerStreamMin());
    }

    public String getMainBowerStringMin() {
        return bytesToString(getMainBowerBytesMin());
    }

    public String getMainBowerNameMap() {
        return getMainBowerPrefixVersioned() + ".min.js.map";
    }

    public InputStream getMainBowerStreamMap() {
        return stream(getMainBowerNameClean(".min.js.map")); // TODO
    }

    public byte[] getMainBowerBytesMap() {
        return bytes(getMainBowerStreamMap());
    }

    public String getMainBowerStringMap() {
        return bytesToString(getMainBowerBytesMap());
    }

    public String mainBowerStringMapWithVersions() {
        return getMainBowerStringMap(getMainBowerNameMin(), getMainBowerName());
    }

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
        return cl.getResourceAsStream("META-INF/resources/org/npmaven/" + name + "/" + rsrc);
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
