package cn.iamding.drest.exception;

import java.io.UncheckedIOException;

import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * 把强制检查的json转换异常转换成运行时异常，避免大量没有意义的try-catch
 */
public class UncheckedJsonProcessingException extends UncheckedIOException {
    public UncheckedJsonProcessingException(JsonProcessingException cause) {
        super(cause.getMessage(), cause);
    }
}