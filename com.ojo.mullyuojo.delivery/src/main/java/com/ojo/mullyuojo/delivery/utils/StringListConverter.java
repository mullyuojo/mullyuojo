package com.ojo.mullyuojo.delivery.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference; // TypeReference 추가
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.ArrayList;
import java.util.List;

@Converter
public class StringListConverter implements AttributeConverter<List<Long>, String> {

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(List<Long> dataList) {
        if (dataList == null) {
            return null; // null 값 처리
        }
        try {
            return mapper.writeValueAsString(dataList);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON serialization error", e);
        }
    }

    @Override
    public List<Long> convertToEntityAttribute(String data) {
        if (data == null) {
            return new ArrayList<>(); // null 값 처리
        }
        try {
            // Long 타입 리스트로 정확히 읽어오기 위해 TypeReference 사용
            return mapper.readValue(data, new TypeReference<List<Long>>() {});
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON deserialization error", e);
        }
    }
}