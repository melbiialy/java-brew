package http.scanner;

import http.annotation.Controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ControllerScanner implements ClassPathScanner{
    private final Logger logger = LoggerFactory.getLogger(ControllerScanner.class.getName());

    @Override
    public List<Class<?>> scan(String basePackage) {
        String packagePath = basePackage.replace('.','/');
        List<Class<?>> classes = new ArrayList<>();
        logger.info("Scanning package: {}", packagePath);
        try {
            File[] files = getFiles(packagePath);
            for (File file : files){

                if (!file.getName().contains(".class")) {
                    logger.trace("Scanning subpackage: {}.{}", packagePath, file.getName());

                    String subPackagePath = packagePath.isEmpty() ? file.getName() : packagePath + "." + file.getName();
                    scan(subPackagePath);
                    continue;
                }
                String className = basePackage.isEmpty() ? file.getName().replace(".class", "") : basePackage + "." + file.getName().replace(".class", "");
                Class<?> clazz = Class.forName(className);
                if (clazz.isAnnotationPresent(Controller.class)) {
                    logger.trace("Registering controller: {}", className);
                    classes.add(clazz);
                    logger.trace("Controller registered: {}", className);

                }
            }


        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return classes;
    }


    private  File[] getFiles(String basePackage) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        URL resource = classLoader.getResource(basePackage);
        if (resource == null) {
            logger.warn("No resources found for base package: {}", basePackage);
            return new File[0];
        }
        File directory = new File(resource.getFile());
        File[] files = directory.listFiles();
        if (files == null){
            logger.warn("No files found for base package: {}", basePackage);
            return new File[0];
        }
        return files;
    }
}
