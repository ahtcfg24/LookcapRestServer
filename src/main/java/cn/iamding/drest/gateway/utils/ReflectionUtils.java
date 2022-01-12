package cn.iamding.drest.gateway.utils;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 反射相关工具类
 * Created on 2022-01-12
 */
public class ReflectionUtils {

    /**
     * 根据类上的注解扫描指定包路径下的所有类
     *
     * @param packageName 包路径
     * @param annotation 类上的注解class
     * @return 被传入的注解标记的Class对象
     */
    public static Set<Class<?>> scanClassByClassAnnotation(String packageName, Class<? extends Annotation> annotation)
            throws IOException, ClassNotFoundException {
        return ReflectionUtils.scanClassFromPackage(packageName).values().stream()
                .filter(clz -> clz.getDeclaredAnnotation(annotation) != null)
                .collect(Collectors.toSet());
    }

    /**
     * 从指定Java Package中扫描class文件，扫描范围包括package的子目录
     *
     * @param packageName Java package全名
     * @return 扫描出来的Class对象Map，Key是类全名，value是Class对象
     * @throws IOException 扫描过程可能抛出IO异常
     */
    private static Map<String, Class<?>> scanClassFromPackage(String packageName)
            throws IOException, ClassNotFoundException {
        Map<String, Class<?>> classMap = new HashMap<>();

        String path = buildFileDirPath(packageName);
        Enumeration<URL> urls = Thread.currentThread().getContextClassLoader().getResources(path);
        while (urls != null && urls.hasMoreElements()) {
            URL url = urls.nextElement();
            if ("file".equals(url.getProtocol())) {
                String dirName = URLDecoder.decode(url.getFile(), "UTF-8");
                File dir = new File(dirName);
                if (dir.isDirectory()) {
                    scanClassNameToMap(dir, classMap);
                } else {
                    throw new IllegalArgumentException("packageName is not directory: " + packageName);
                }
            }
        }
        return classMap;
    }

    /**
     * 把package路径替换为class文件夹路径
     */
    private static String buildFileDirPath(String packageName) {
        if (packageName != null) {
            String resourceName = packageName.replace(".", "/");
            resourceName = resourceName.replace("\\", "/");
            if (resourceName.startsWith("/")) {
                resourceName = resourceName.substring(1);
            }
            if (resourceName.endsWith("/")) {
                resourceName = resourceName.substring(0, resourceName.length() - 1);
            }
            return resourceName;
        }
        return null;
    }

    /**
     * 递归把文件夹下存在的类文件名提取到classMap中
     */
    private static void scanClassNameToMap(File dir, Map<String, Class<?>> classMap)
            throws ClassNotFoundException {
        if (dir.isDirectory()) {
            File[] files = dir.listFiles();
            for (File file : files) {
                scanClassNameToMap(file, classMap);
            }
        } else if (dir.getName().endsWith(".class") && !dir.getName().contains("$")) { //只提取正常类
            String name = dir.getPath();
            //去掉classes文件夹
            name = name.substring(name.indexOf("classes") + 8).replace("\\", ".").replace("/", ".");
            if (!classMap.containsKey(name)) {
                //class名称去掉尾部的".class"6个字符
                String className = name.substring(0, name.length() - 6);
                classMap.put(name, Class.forName(className));
            }
        }
    }

}
