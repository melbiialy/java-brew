package http.routing;

import http.annotation.Controller;
import http.annotation.EndPoint;
import http.annotation.PathVariable;
import http.enums.HttpMethod;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A class responsible for discovering and registering endpoints within a specified package and mapping them
 * to application routes using a given {@link Router} instance. It processes classes annotated with {@code @Controller}
 * and methods within those classes annotated with {@code @EndPoint}, allowing for dynamic route mapping in the application.
 *
 * Functionality includes:
 * - Scanning packages for annotated classes and methods.
 * - Registering routes based on annotations defined in controllers and methods.
 * - Managing endpoints within a specified package hierarchy recursively.
 *
 * The class supports mapping HTTP methods, path definitions, and parameter information, ensuring a scalable and
 * annotation-driven approach to route registration in web frameworks.
 */

public  class EndPointRegistry {

    public  void registerEndPoints(String basePackage, Router router) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        String packagePath = basePackage.replace('.','/');
        File[] files = getFiles(packagePath);
        for (File file : files){

            if (!file.getName().contains(".class")){
                System.out.println("Registering subpackage: " + basePackage+"."+file.getName());
                String subPackagePath = basePackage.isEmpty()?file.getName():basePackage+"."+file.getName();
                registerEndPoints(subPackagePath, router);
                continue;
            }

            String className = basePackage.isEmpty()?file.getName().replace(".class",""):basePackage+"."+file.getName().replace(".class","");
            Class<?> clazz = Class.forName(className);
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

    private static void registerEndpoints(Router router, Method[] methods, String basePath, Object controllerInstance) {
        for (Method method : methods){
            if (method.isAnnotationPresent(EndPoint.class)){
                EndPoint endPoint = method.getAnnotation(EndPoint.class);
                String path = basePath + endPoint.path();
                HttpMethod httpMethod = endPoint.method();
                List<ParameterInfo> parameterInfos = getParameterInfos(method);
                EndpointDefinition endpointDefinition = new EndpointDefinition(controllerInstance, method, parameterInfos);
                router.addRoute(path, httpMethod, endpointDefinition);
            }
        }
    }

    private static List<ParameterInfo> getParameterInfos(Method method) {
        Parameter[] parameters = method.getParameters();
        List<ParameterInfo> parameterInfos = new ArrayList<>();
        for (Parameter parameter : parameters){
            String parameterName = parameter.getName();
            if (parameter.isAnnotationPresent(PathVariable.class)){
                parameterName = parameter.getAnnotation(PathVariable.class).value();
            }
            parameterInfos.add(new ParameterInfo(parameterName,parameter.getType()));
        }
        return parameterInfos;
    }

    private static File[] getFiles(String basePackage) {
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
