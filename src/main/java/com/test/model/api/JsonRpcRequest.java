package com.test.model.api;

import lombok.Data;

import java.util.Map;

@Data
public class JsonRpcRequest {
    private String jsonrpc;
    private String method;
    private Map<String, Object> params;
    private String id;

    public Object getParam(String name) {
        if (params == null) {
            return null;
        }
        return params.get(name);
    }
}
