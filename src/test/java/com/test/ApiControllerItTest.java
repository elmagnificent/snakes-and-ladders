package com.test;

import com.test.model.ProcessingException;
import com.test.model.api.GameDTO;
import com.test.model.api.JsonRpcRequest;
import com.test.model.api.JsonRpcResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Created by Michael on 12.04.2021.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = ApiControllerItTest.class)
public class ApiControllerItTest {
    @Autowired
    private TestRestTemplate template;

    @Test
    public void getNull_shouldCreateNew() {
        JsonRpcRequest request = new JsonRpcRequest();
        request.setJsonrpc("2.0");
        request.setId("1");
        request.setMethod("getGame");
        ResponseEntity<JsonRpcResponse<GameDTO>> response = post("", request,
            new ParameterizedTypeReference<JsonRpcResponse<GameDTO>>() {
            });
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("1", response.getBody().getId());
        assertEquals(1, response.getBody().getResult().getPlayerPosition());
    }

    @Test
    public void getExisting_shouldReturn() {
        JsonRpcRequest request = new JsonRpcRequest();
        request.setJsonrpc("2.0");
        request.setId("1");
        request.setMethod("getGame");
        request.setParams(Collections.singletonMap("gameId", 1));
        ResponseEntity<JsonRpcResponse<GameDTO>> response = post("", request,
            new ParameterizedTypeReference<JsonRpcResponse<GameDTO>>() {
            });
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("1", response.getBody().getId());
        assertEquals(1, response.getBody().getResult().getId());
    }

    @Test
    public void moveToken_shouldBeMoved() {
        JsonRpcRequest request = new JsonRpcRequest();
        request.setJsonrpc("2.0");
        request.setId("1");
        request.setMethod("getGame");
        Map<String, Object> params = new HashMap<>();
        params.put("gameId", 1);
        request.setParams(params);
        ResponseEntity<JsonRpcResponse<GameDTO>> response = post("", request,
            new ParameterizedTypeReference<JsonRpcResponse<GameDTO>>() {
            });
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("1", response.getBody().getId());
        assertEquals(1, response.getBody().getResult().getId());
        int tokenPosition = response.getBody().getResult().getPlayerPosition();

        params.put("tokenMoved", 3);
        tokenPosition += 3;
        request.setMethod("move");
        response = post("", request, new ParameterizedTypeReference<JsonRpcResponse<GameDTO>>() {
        });
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("1", response.getBody().getId());
        assertEquals(tokenPosition, response.getBody().getResult().getPlayerPosition());

        params.put("tokenMoved", 4);
        tokenPosition += 4;
        response = post("", request, new ParameterizedTypeReference<JsonRpcResponse<GameDTO>>() {
        });
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("1", response.getBody().getId());
        assertEquals(tokenPosition, response.getBody().getResult().getPlayerPosition());
    }

    @Test
    public void rollDice_shouldReturn() {
        JsonRpcRequest request = new JsonRpcRequest();
        request.setJsonrpc("2.0");
        request.setId("1");
        request.setMethod("rollDice");
        ResponseEntity<JsonRpcResponse<Integer>> response = post("", request,
            new ParameterizedTypeReference<JsonRpcResponse<Integer>>() {
            });
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("1", response.getBody().getId());
        assertTrue(response.getBody().getResult() > 0);
        assertTrue(response.getBody().getResult() < 7);
    }

    @Test
    public void moveIllegal_shouldReturn() {
        JsonRpcRequest request = new JsonRpcRequest();
        request.setJsonrpc("2.0");
        request.setId("1");
        request.setMethod("move");
        Map<String, Object> params = new HashMap<>();
        params.put("gameId", 1);
        params.put("tokenMoved", 10);
        request.setParams(params);
        ResponseEntity<JsonRpcResponse<GameDTO>> response = post("", request,
            new ParameterizedTypeReference<JsonRpcResponse<GameDTO>>() {
            });
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(ProcessingException.INVALID_PARAMS, response.getBody().getError().getCode());
    }

    private <T> ResponseEntity<T> post(String urlSuffix, Object payload, ParameterizedTypeReference<T> responseType) {
        return template.exchange("/api/v1" + urlSuffix, HttpMethod.POST, new HttpEntity<>(payload, null), responseType);
    }
}
