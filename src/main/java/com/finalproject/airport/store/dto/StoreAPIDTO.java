package com.finalproject.airport.store.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class StoreAPIDTO {

    //입/출국구역 A: 입국장, D: 출국장
    private String arrordep;
    //매장명
    private String entrpskoreannm;
    //위치
    private String lckoreannm;
    //영업시간
    private String servicetime;
    //전화번호
    private String tel;
    //취급품목
    private String trtmntprdlstkoreannm;
}
