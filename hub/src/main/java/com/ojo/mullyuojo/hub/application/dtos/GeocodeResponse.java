package com.ojo.mullyuojo.hub.application.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true) // 응답에 우리가 정의하지 않은 필드가 있어도 무시
public class GeocodeResponse {

    private String status;
    private Meta meta;
    private List<Address> addresses;

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Meta {
        private int totalCount;
    }

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Address {
        private String roadAddress;
        private String jibunAddress;
        private String x; // 경도
        private String y; // 위도
    }
}
