package com.test.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.model.ProcessingException;
import com.test.model.api.JsonRpcRequest;
import com.test.model.api.JsonRpcResponse;
import com.test.services.GameService;
import com.test.utils.ParsingUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.concurrent.CompletableFuture;

/**
 * Created by Michael on 12.04.2021.
 */
@RestController
@RequestMapping("/api/v1")
public class ApiController {
    private static final long REQUEST_TIMEOUT_MS = 30_000;

    private final ObjectMapper objectMapper;
    private final GameService gameService;

    private final ResponseEntity<JsonRpcResponse> timeoutResponse;
    private final ResponseEntity<JsonRpcResponse> serverErrorResponse;

    @Autowired
    public ApiController(ObjectMapper objectMapper, GameService gameService) {
        this.objectMapper = objectMapper;
        this.gameService = gameService;

        final JsonRpcResponse timeoutJsonRpcResponse
            = JsonRpcResponse.newError(null, ProcessingException.REQUEST_TIMED_OUT, "Processing");
        timeoutResponse = new ResponseEntity<>(timeoutJsonRpcResponse, HttpStatus.PROCESSING);

        final JsonRpcResponse serverErrorJsonRpcResponse
            = JsonRpcResponse.newError(null, ProcessingException.INTERNAL_ERROR, "Internal Server Error");
        serverErrorResponse = new ResponseEntity<>(serverErrorJsonRpcResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PostMapping
    public DeferredResult<ResponseEntity<JsonRpcResponse>> process(@RequestBody String requestStr) {
        DeferredResult<ResponseEntity<JsonRpcResponse>> deferredResult = new DeferredResult<>(REQUEST_TIMEOUT_MS, timeoutResponse);
        CompletableFuture.supplyAsync(()
            -> dispatch(requestStr))
            .whenCompleteAsync((result, throwable) -> {
                if (throwable == null) {
                    deferredResult.setResult(result);
                } else {
                    deferredResult.setResult(serverErrorResponse);
                }
            });

        return deferredResult;
    }

    private ResponseEntity<JsonRpcResponse> dispatch(String requestStr) {
        final JsonRpcRequest request;
        try {
            request = objectMapper.readValue(requestStr, JsonRpcRequest.class);
        } catch (Exception e) {
            final JsonRpcResponse response = JsonRpcResponse.newError(null, ProcessingException.PARSE_ERROR, "Parse error");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        try {
            final String method = request.getMethod();
            if (method == null) {
                return response(request, HttpStatus.BAD_REQUEST, "Missing method");
            }

            if ("getGame".equals(method)) {
                return getGame(request);
            } else if ("move".equals(method)) {
                return moveToken(request);
            }

            final JsonRpcResponse response
                = JsonRpcResponse.newError(request.getId(), ProcessingException.METHOD_NOT_FOUND, "Unknown method");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (ProcessingException e) {
            final JsonRpcResponse response = JsonRpcResponse.newError(request.getId(), e.getCode(), e.getMessage());
            return new ResponseEntity<>(response, e.getHttpStatus());
        }
    }

    private ResponseEntity<JsonRpcResponse> getGame(JsonRpcRequest request) {
        Long gameId = ParsingUtils.getNullableLongParam(request, "gameId");
        final JsonRpcResponse response = JsonRpcResponse.newResult(request.getId(), gameService.get(gameId));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private ResponseEntity<JsonRpcResponse> moveToken(JsonRpcRequest request) {
        Long gameId = ParsingUtils.getNullableLongParam(request, "gameId");
        int tokenMoved = ParsingUtils.getIntParam(request, "tokenMoved");
        final JsonRpcResponse response = JsonRpcResponse.newResult(request.getId(), gameService.moveToken(gameId, tokenMoved));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private ResponseEntity<JsonRpcResponse> response(JsonRpcRequest request, HttpStatus httpStatus, String resultMessage) {
        return new ResponseEntity<>(JsonRpcResponse.newResult(request.getId(), new Object() {
            private final String message = resultMessage;
        }), httpStatus);
    }
}
