package cn.iamding.drest.gateway.utils;

import static com.fasterxml.jackson.core.JsonFactory.Feature.INTERN_FIELD_NAMES;

import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import com.fasterxml.jackson.core.JsonFactoryBuilder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import cn.iamding.drest.exception.UncheckedJsonProcessingException;


/**
 * 对Jackson的封装类
 */
public final class ObjectMapperUtils {

    private static final DateFormat DF = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSSZ");
    private static final ObjectMapper MAPPER =
            new ObjectMapper(new JsonFactoryBuilder().configure(INTERN_FIELD_NAMES, false).build())
                    .setDateFormat(DF); //修改时间戳返回格式

    public static String toJSON(Object obj) {
        if (obj == null) {
            return null;
        }
        try {
            return MAPPER.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new UncheckedJsonProcessingException(e);
        }
    }


    public static void toJSON(Object obj, OutputStream writer) {
        if (obj == null) {
            return;
        }
        try {
            MAPPER.writeValue(writer, obj);
        } catch (IOException e) {
            throw ExceptionUtils.wrapException(e);
        }
    }


    public static <T> T fromJSON(String json, Class<T> valueType) {
        if (json == null) {
            return null;
        }
        try {
            return MAPPER.readValue(json, valueType);
        } catch (IOException e) {
            throw ExceptionUtils.wrapException(e);
        }
    }

}