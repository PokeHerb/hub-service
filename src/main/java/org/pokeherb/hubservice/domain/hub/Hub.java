package org.pokeherb.hubservice.domain.hub;

import jakarta.persistence.*;
import lombok.*;
import org.pokeherb.hubservice.global.domain.Auditable;

import java.util.List;
import java.util.UUID;
/**
 * 1. 모든 사용자가 조회 가능
 * 2. 생성, 수정, 삭제는 마스터 관리자만 가능
 * */
@Entity
@Table(name = "p_hub")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Access(AccessType.FIELD)
@ToString
public class Hub extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID hubId;

    private String hubName;

    @Embedded
    private Address address;

    @Embedded
    private Coordinate coordinate;

    /**
     * 허브 생성은 마스터 관리자만 가능
     * */
    @Builder
    public Hub(String hubName, String sido, String sigungu, String eupmyeon, String dong,
               String ri, String street, String building_no, String details,
               AddressToCoordinateConverter converter, CheckAccessHub checkAccessHub) {
        checkAccessHub.checkAccess();
        this.hubName = hubName;
        this.address = Address.builder()
                .sido(sido)
                .sigungu(sigungu)
                .eupmyeon(eupmyeon)
                .dong(dong)
                .ri(ri)
                .street(street)
                .building_no(building_no)
                .details(details)
                .build();
        setCoordinate(address, converter);
    }

    /**
     * String 주소를 위도, 경도로 변환하여 저장
     * */
    private void setCoordinate(Address address, AddressToCoordinateConverter converter) {
        List<Double> coordinates = converter.convert(address.toString());
        this.coordinate = Coordinate.builder()
                .latitude(coordinates.get(0))
                .longitude(coordinates.get(1))
                .build();
    }

    /**
     * 허브 이름 수정은 마스터 관리자만 가능
     * */
    public void changeHubName(String hubName, CheckAccessHub checkAccessHub) {
        checkAccessHub.checkAccess();
        this.hubName = hubName;
    }

    /**
     * 허브 주소 수정은 마스터 관리자만 가능
     * */
    public void changeAddress(String sido, String sigungu, String eupmyeon, String dong,
                              String ri, String street, String building_no, String details,
                              AddressToCoordinateConverter converter, CheckAccessHub checkAccessHub) {
        checkAccessHub.checkAccess();
        this.address = Address.builder()
                .sido(sido)
                .sigungu(sigungu)
                .eupmyeon(eupmyeon)
                .dong(dong)
                .ri(ri)
                .street(street)
                .building_no(building_no)
                .details(details)
                .build();
        setCoordinate(address, converter);
    }

    /**
     * 허브 삭제는 soft delete로 구현
     * 허브 삭제는 마스터 관리자만 가능
     * */
    public void deleteHub(String username, CheckAccessHub checkAccessHub) {
        checkAccessHub.checkAccess();
        this.softDelete(username);
    }
}
