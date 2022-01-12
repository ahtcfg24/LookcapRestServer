package cn.iamding.drest.gateway.converter;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.iamding.drest.gateway.utils.IOUtils;
import cn.iamding.drest.gateway.utils.ObjectMapperUtils;

/**
 * HTTP消息体序列化工具
 * Created on 2022-01-12
 */
public class HttpMessageConverter {

    public static Object readReqBodyToObj(HttpServletRequest request, Class<?> parameterType) throws IOException {
        String body = IOUtils.readStringBodyFromRequest(request);
        return ObjectMapperUtils.fromJSON(body, parameterType);
    }

    public static void writeJsonResponse(HttpServletResponse response, Object bizResponse)
            throws IOException {
        ObjectMapperUtils.toJSON(bizResponse, response.getOutputStream());
    }
}
