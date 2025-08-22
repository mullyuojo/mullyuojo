package com.ojo.mullyuojo.hub.application.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ojo.mullyuojo.hub.application.dtos.GeocodeResponse;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;


@Service
public class GeoService {

    private final String NAVER_CLIENT_ID = "evd29haomr";
    private final String NAVER_CLIENT_SECRET = "7UKSgU5cYJw2NfzmMYWdL9TErvdnqV1jKKFgP43q";

    public GeocodeResponse getGeocodeResponse(String address) {
        try {
            String url = UriComponentsBuilder
                    .fromUriString("https://maps.apigw.ntruss.com/map-geocode/v2/geocode")
                    .queryParam("query", address)
                    .build()
                    .toUriString();

            HttpHeaders headers = new HttpHeaders();
            headers.set("X-NCP-APIGW-API-KEY-ID", NAVER_CLIENT_ID);
            headers.set("X-NCP-APIGW-API-KEY", NAVER_CLIENT_SECRET);

            HttpEntity<Void> entity = new HttpEntity<>(headers); // body없이 header만 있는 Http객체 생성

            RestTemplate restTemplate = new RestTemplate();  // Http 클라이언트 클래스
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
                                              // restTemplate 으로 응답을 String 으로 받아옴
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                ObjectMapper mapper = new ObjectMapper(); // objctMapper는 jackson의 핵심 클래스(json - java객체 변환 담당)
                return mapper.readValue(response.getBody(), GeocodeResponse.class);
            }                 // readValue는 json 문자열을 지정타입 객체(GeocodeResponse.class)로 역직렬화
        } catch (Exception e) {
            throw new RuntimeException("Geocode API 호출 실패: " + e.getMessage(), e);
        }
        return null;
    }
}
