package com.test.utils;

import com.test.model.ProcessingException;
import com.test.model.api.JsonRpcRequest;
import org.springframework.lang.Nullable;

/**
 * Created by Michael on 12.04.2021.
 */
public class ParsingUtils {
    public static int getIntParam(JsonRpcRequest request, String name) {
        final Object obj = request.getParam(name);
        if (!(obj instanceof Integer)) {
            throw new ProcessingException(ProcessingException.INVALID_PARAMS, "Invalid or missing parameter: " + name);
        }
        return (int) obj;
    }

    @Nullable
    public static Long getNullableLongParam(JsonRpcRequest request, String name) {
        final Object obj = request.getParam(name);
        if (!(obj instanceof Long || obj instanceof Integer) && obj != null) {
            throw new ProcessingException(ProcessingException.INVALID_PARAMS, "Invalid or missing parameter: " + name);
        }
        return obj == null ? null : (obj instanceof Long ? (Long) obj : ((Integer) obj).longValue());
    }
}
