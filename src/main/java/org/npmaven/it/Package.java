package org.npmaven.it;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

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
        return getMainBowerPrefix() + ".js";
    }

    public String getMainBowerNameMin() {
        return getMainBowerPrefix() + ".min.js";
    }

    public String getMainBowerNameMap() {
        return getMainBowerPrefix() + ".min.js.map";
    }

    public String getMainBowerPrefix() {
        String main = props.getProperty("main.bower");

        // Strip leading ./ if there is one
        if(main.startsWith("./")) main = main.substring(2);

        // Strip .js off the end
        main = main.substring(0, main.length()-3);

        return main + "-" + getVersion();
    }

    private InputStream stream(String rsrc) {
        return cl.getResourceAsStream("META-INF/resources/org/npmaven/" + name + "/" + rsrc);
    }
}
