package cn.iamding.drest.gateway;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import cn.iamding.drest.annotation.RestPath;
import cn.iamding.drest.gateway.utils.ReflectionUtils;

/**
 * RestPath注解的解析器，用于注册POST类型的HTTP方法
 * Created on 2022-01-12
 */
public class RestPathResolver {

    //Map内只存放POST方法，key=uri  value=被注解的Method
    private final Map<String, Method> httpMethodMap = new HashMap<>();

    //Map内存放POST方法所在的Controller对象，key=uri  value=被注解的Method所在的类对象
    private final Map<String, Object> httpControllerMap = new HashMap<>();

    public RestPathResolver() {
        this("cn.iamding.drest.service");
    }

    public RestPathResolver(String scanPath) {
        scanRestPathMethod(scanPath);
    }

    private void scanRestPathMethod(String packagePath) {
        try {
            Set<Class<?>> restClassSet =
                    ReflectionUtils.scanClassByClassAnnotation(packagePath, RestPath.class);
            for (Class<?> clz : restClassSet) {
                String rootPath = clz.getAnnotation(RestPath.class).value();
                //为该类创建Controller对象
                Object controllerObj = clz.getDeclaredConstructor().newInstance();
                for (Method method : clz.getDeclaredMethods()) {
                    RestPath methodAnnotation = method.getAnnotation(RestPath.class);
                    if (methodAnnotation == null) { //未被注解的方法不注册
                        continue;
                    }
                    String uri = rootPath + methodAnnotation.value();
                    httpControllerMap.put(uri, controllerObj);
                    httpMethodMap.put(uri, method);
                }
            }
        } catch (IOException | ClassNotFoundException | InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            // 初始化阶段理论不应该发生这些异常，发生了启动起来也没用，直接退出程序
            System.exit(-1);
        }
    }

    public Map<String, Method> getHttpMethodMap() {
        return httpMethodMap;
    }

    public Map<String, Object> getHttpControllerMap() {
        return httpControllerMap;
    }

}
