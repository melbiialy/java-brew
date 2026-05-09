package http.scanner;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseScanner implements ClassPathScanner{
    private final Logger logger = LoggerFactory.getLogger(BaseScanner.class.getName());
    public static  String DEFAULT_PACKAGE = "application";

    public abstract boolean matches(Class<?> clazz);
    @Override
    public List<Class<?>> scan(String basePackage) {
        String packagePath = basePackage.replace('.', '/');
        List<Class<?>> classes = new ArrayList<>();
        logger.info("Scanning package: {}", basePackage);

        try {
            File[] files = getFiles(packagePath);

            for (File file : files) {
                if (file.isDirectory()) {
                    String subPackage = basePackage.isEmpty()
                            ? file.getName()
                            : basePackage + "." + file.getName();
                    logger.trace("Scanning subpackage: {}", subPackage);
                    classes.addAll(scan(subPackage));
                    continue;
                }

                if (!file.getName().endsWith(".class")) {
                    continue;
                }

                String className = basePackage.isEmpty()
                        ? file.getName().replace(".class", "")
                        : basePackage + "." + file.getName().replace(".class", "");

                Class<?> clazz = Class.forName(className);

                if (matches(clazz)) {
                    classes.add(clazz);
                }
            }

        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Failed to load class during scan of: " + basePackage, e);
        }

        return classes;
    }
    @Override
    public List<Class<?>> scan() {
        return scan(DEFAULT_PACKAGE);
    }


    private File[] getFiles(String basePackage) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        URL resource = classLoader.getResource(basePackage);

        if (resource == null) {
            logger.warn("No resources found for base package: {}", basePackage);
            return new File[0];
        }

        try {
            File directory = new File(resource.toURI());
            File[] files = directory.listFiles();

            if (files == null) {
                logger.warn("Path is not a directory: {}", directory.getAbsolutePath());
                return new File[0];
            }

            return files;

        } catch (URISyntaxException e) {
            throw new RuntimeException("Invalid resource path: " + resource, e);
        }
    }
}
