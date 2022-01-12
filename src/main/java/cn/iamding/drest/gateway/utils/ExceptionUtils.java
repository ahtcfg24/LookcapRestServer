package cn.iamding.drest.gateway.utils;

import java.io.IOException;
import java.io.UncheckedIOException;

import com.fasterxml.jackson.core.JsonProcessingException;

import cn.iamding.drest.exception.UncheckedJsonProcessingException;

/**
 * 这是一个默认的描述
 * Created on 2022-01-12
 */
public class ExceptionUtils {

    public static RuntimeException wrapException(IOException e) {
        if (e instanceof JsonProcessingException) {
            return new UncheckedJsonProcessingException((JsonProcessingException) e);
        } else {
            return new UncheckedIOException(e);
        }
    }
}
