package com.guba.app.service.remote;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

public class Converter {

    private ObjectMapper objectMapper = new ObjectMapper();

    public <T> List<T> convertir(String body, TypeReference<List<T>> typeReference ){
        try {
            return objectMapper.readValue(body,typeReference );
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public <T> T getData(String body, Class<T> tClass){
        try {
            return objectMapper.readValue(body,tClass );
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
