package com.test.model.api;

import lombok.Data;

@Data
public class JsonRpcResponse<T> {
    private final String jsonrpc = "2.0";
    private final T result;
    private final JsonRpcError error;
    private final String id;

    private JsonRpcResponse(String id, T result, JsonRpcError error) {
        this.result = result;
        this.error = error;
        this.id = id;
    }

    public static <T> JsonRpcResponse<T> newResult(String id, T result) {
        return new JsonRpcResponse<>(id, result, null);
    }

    public static <T> JsonRpcResponse<T> newError(String id, int code, String message) {
        JsonRpcError error = new JsonRpcError(code, message);
        return new JsonRpcResponse<>(id, null, error);
    }

    @Data
    public static class JsonRpcError {
        private final int code;
        private final String message;

        private JsonRpcError(int code, String message) {
            this.code = code;
            this.message = message;
        }
    }
}
