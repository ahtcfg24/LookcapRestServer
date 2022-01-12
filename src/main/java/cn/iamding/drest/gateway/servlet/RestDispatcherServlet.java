package cn.iamding.drest.gateway.servlet;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.iamding.drest.gateway.RestPathResolver;
import cn.iamding.drest.gateway.converter.HttpMessageConverter;

/**
 * 处理所有rest请求
 * Created on 2022-01-12
 */
public class RestDispatcherServlet extends HttpServlet {

    private static final int SUCCESS_CODE = 200;
    private static final int NOT_FOUND_CODE = 404;
    private static final int INVALID_CONTENT_CODE = 405;
    private static final int UN_SUPPORT_REQUEST = 406;
    private static final int SERVER_ERROR = 500;

    private static final String JSON_CONTENT_TYPE = "application/json";
    private static final String UTF_8_CHARSET = "charset=UTF-8";
    private static final String JSON_UTF8_CONTENT = JSON_CONTENT_TYPE + ";" + UTF_8_CHARSET;

    private final RestPathResolver restPathResolver;

    public RestDispatcherServlet() {
        restPathResolver = new RestPathResolver();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //非json格式不支持
        if (!checkJsonContentType(request.getContentType())) {
            buildErrorResponse(response, INVALID_CONTENT_CODE,
                    "post request must set contentType:" + JSON_UTF8_CONTENT);
            return;
        }
        String uri = request.getRequestURI();
        Map<String, Method> apiMap = restPathResolver.getHttpMethodMap();
        Method method = apiMap.get(uri);
        //找不到uri返回404
        if (method == null) {
            buildErrorResponse(response, NOT_FOUND_CODE, "not found POST resource of uri:" + uri);
            return;
        }
        Object controllerObj = restPathResolver.getHttpControllerMap().get(uri);
        Class<?>[] parameterTypes = method.getParameterTypes();
        //服务方法多个参数时不支持
        if (parameterTypes.length > 1) {
            buildErrorResponse(response, UN_SUPPORT_REQUEST, "not find service param fit the request body");
            return;
        }
        //调用业务方法获得返回值
        Object bizResponse;
        try {
            if (parameterTypes.length == 0) {
                bizResponse = method.invoke(controllerObj);
            } else {
                Object requestBody = HttpMessageConverter.readReqBodyToObj(request, parameterTypes[0]);
                bizResponse = method.invoke(controllerObj, requestBody);
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            buildErrorResponse(response, SERVER_ERROR, "Server handle request exception message:" + e.getMessage());
            return;
        }
        //返回json
        response.setStatus(SUCCESS_CODE);
        response.setContentType(JSON_UTF8_CONTENT);
        HttpMessageConverter.writeJsonResponse(response, bizResponse);
    }

    private void buildErrorResponse(HttpServletResponse response, int status, String msg) throws IOException {
        response.setStatus(status);
        response.getWriter().println(msg);
    }

    private boolean checkJsonContentType(String contentType) {
        Set<String> contentTypeSet = new HashSet<>();
        Collections.addAll(contentTypeSet, contentType.split(";"));
        return contentTypeSet.contains(JSON_CONTENT_TYPE) && contentTypeSet.contains(UTF_8_CHARSET);
    }
}
