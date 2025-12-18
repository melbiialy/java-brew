package http.routing;

import http.annotation.Controller;
import http.annotation.EndPoint;
import http.enums.HttpMethod;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.HashMap;

public class EndPointRegistry {

    public void registerEndPoints(String basePackage, Router router) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        basePackage = basePackage.replace('.','/');
        File[] files = getFiles(basePackage);
        for (File file : files){
            if (!file.getName().endsWith(".class")) continue;
            Class<?> clazz = Class.forName(basePackage+"." + file.getName().replace(".class", ""));
            if (clazz.isAnnotationPresent(Controller.class)) {
                System.out.println("Registering controller: " + clazz.getName());
                String basePath = clazz.getAnnotation(Controller.class).path();
                Method[] methods = clazz.getMethods();
                // todo handle to call the application context to get the controller object
                Object controllerInstance = clazz.getDeclaredConstructor().newInstance();

                registerEndpoints(router, methods, basePath, controllerInstance);

                System.out.println("Registered controller: " + clazz.getName());
            }
        }
    }

    private void registerEndpoints(Router router, Method[] methods, String basePath, Object controllerInstance) {
        for (Method method : methods){
            if (method.isAnnotationPresent(EndPoint.class)){
                EndPoint endPoint = method.getAnnotation(EndPoint.class);
                String path = basePath + endPoint.path();
                HttpMethod httpMethod = endPoint.method();
                Parameter[] parameters = method.getParameters();
                HashMap<String, Type> parametersMap = new HashMap<>();
                for (Parameter parameter : parameters){
                    parametersMap.put(parameter.getName(), parameter.getParameterizedType());
                }
                EndpointDefinition endpointDefinition = new EndpointDefinition(controllerInstance, method, parametersMap);
                router.addRoute(path, httpMethod, endpointDefinition);
            }
        }
    }

    private  File[] getFiles(String basePackage) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        URL resource = classLoader.getResource(basePackage);
        if (resource == null) {
            throw new RuntimeException("No resources found for base package: " + basePackage);
        }
        File directory = new File(resource.getFile());
        File[] files = directory.listFiles();
        if (files == null){
            throw new RuntimeException("No files found for base package: " + basePackage);
        }
        return files;
    }
}
