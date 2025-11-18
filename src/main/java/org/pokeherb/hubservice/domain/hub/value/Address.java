package org.pokeherb.hubservice.domain.hub.value;

import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Address {
    // 시/도
    private String sido;
    // 시/군/구
    private String sigungu;
    // 읍/면
    private String eupmyeon;
    // 동
    private String dong;
    // 리
    private String ri;
    // 도로명 주소
    private String street;
    // 건물 번호
    private String building_no;
    // 나머지 상세 주소
    private String details;

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

    /**
     * Address 객체를 문자열로 변환
     * */
    @Override
    public String toString() {
        String address = "";
        if (sido != null) {
            address += sido + " ";
        }
        if (sigungu != null) {
            address += sigungu + " ";
        }
        if (eupmyeon != null) {
            address += eupmyeon + " ";
        }
        if (dong != null) {
            address += dong + " ";
        }
        if (ri != null) {
            address += ri + " ";
        }
        if (street != null) {
            address += street + " ";
        }
        if (building_no != null) {
            address += building_no + " ";
        }
        if (details != null) {
            address += details;
        }
        return address;
    }
}
