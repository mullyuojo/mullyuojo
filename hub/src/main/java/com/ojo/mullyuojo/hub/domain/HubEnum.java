package com.ojo.mullyuojo.hub.domain;

import lombok.Getter;

@Getter
public enum HubEnum {
    SEOUL("서울특별시 센터", "서울특별시 송파구 송파대로 55", 37.5143, 127.1059),
    GYEONGGI_NORTH("경기 북부 센터", "경기도 고양시 덕양구 권율대로 570", 37.6583, 126.8314),
    GYEONGGI_SOUTH("경기 남부 센터", "경기도 이천시 덕평로 257-21", 37.2769, 127.4593),
    BUSAN("부산광역시 센터", "부산 동구 중앙대로 206", 35.1302, 129.0411),
    DAEGU("대구광역시 센터", "대구 북구 태평로 161", 35.8750, 128.6017),
    INCHEON("인천광역시 센터", "인천 남동구 정각로 29", 37.4523, 126.7260),
    GWANGJU("광주광역시 센터", "광주 서구 내방로 111", 35.1578, 126.8495),
    DAEJEON("대전광역시 센터", "대전 서구 둔산로 100", 36.3504, 127.3845),
    ULSAN("울산광역시 센터", "울산 남구 중앙로 201", 35.5396, 129.3160),
    SEJONG("세종특별자치시 센터", "세종특별자치시 한누리대로 2130", 36.4800, 127.2890),
    GANGWON("강원특별자치도 센터", "강원특별자치도 춘천시 중앙로 1", 37.8804, 127.7298),
    CHUNGBUK("충청북도 센터", "충북 청주시 상당구 상당로 82", 36.6380, 127.4890),
    CHUNGNAM("충청남도 센터", "충남 홍성군 홍북읍 충남대로 21", 36.6081, 126.6485),
    JEONBUK("전북특별자치도 센터", "전북 전주시 완산구 효자로 225", 35.8173, 127.1470),
    JEONNAM("전라남도 센터", "전남 무안군 삼향읍 오룡길 1", 34.9953, 126.3751),
    GYEONGBUK("경상북도 센터", "경북 안동시 풍천면 도청대로 455", 36.5697, 128.7254),
    GYEONGNAM("경상남도 센터", "경남 창원시 의창구 중앙대로 300", 35.2340, 128.6811);

    private final String name;
    private final String address;
    private final double latitude;
    private final double longitude;

    HubEnum(String name, String address, double latitude, double longitude) {
        this.name = name;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
    }

}
