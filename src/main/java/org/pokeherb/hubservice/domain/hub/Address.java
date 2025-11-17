package org.pokeherb.hubservice.domain.hub;

import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Address {
    private String sido; // 시/도
    private String sigungu; // 시/군/구
    private String eupmyeon; // 읍/면
    private String dong; // 동
    private String ri; // 리
    private String street; // 도로명 주소
    private String building_no; // 건물 번호
    private String details; // 나머지 상세 주소

    @Builder
    protected Address(String sido, String sigungu, String eupmyeon, String dong, String ri, String street, String building_no, String details) {
        this.sido = sido;
        this.sigungu = sigungu;
        this.eupmyeon = eupmyeon;
        this.dong = dong;
        this.ri = ri;
        this.street = street;
        this.building_no = building_no;
        this.details = details;
    }
}
