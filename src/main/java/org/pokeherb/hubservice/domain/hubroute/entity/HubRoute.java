package org.pokeherb.hubservice.domain.hubroute.entity;

import jakarta.persistence.*;
import lombok.*;
import org.pokeherb.hubservice.domain.hub.service.CheckAccessHub;
import org.pokeherb.hubservice.domain.hubroute.service.TravelInfoCalculator;
import org.pokeherb.hubservice.domain.hubroute.value.TravelInfo;
import org.pokeherb.hubservice.global.domain.Auditable;

import java.util.List;
import java.util.UUID;

/**
 * 1. 허브 간 이동 정보는 모든 사용자가 조회 가능
 * 2. 생성, 수정, 삭제는 마스터 관리자만 가능
 * 3. 허브 수정, 삭제 시 종속적으로 내용 변경
 * 4. 소요시간, 이동거리는 경로 탐색 앱을 활용해 미리 조회 및 저장
 * */
@Entity
@Table(name = "p_hub_route")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Access(AccessType.FIELD)
@ToString
public class HubRoute extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID routingId;

    private UUID startHubId;

    private UUID endHubId;

    @Embedded
    private TravelInfo travelInfo;

    /**
     * 허브 간 이동 정보 생성은 마스터 관리자만 가능
     * 허브가 생성되면 시스템 내부적으로 생성
     * */
    @Builder
    public HubRoute(UUID startHubId, UUID endHubId, TravelInfoCalculator calculator, CheckAccessHub checkAccessHub) {
        checkAccessHub.checkAccess();
        this.startHubId = startHubId;
        this.endHubId = endHubId;
        setTravelInfo(startHubId, endHubId, calculator);
    }

    /**
     * 출발 허브와 목적지 허브 간의 이동거리와 소요시간 구하기
     * */
    private void setTravelInfo(UUID startHubId, UUID endHubId, TravelInfoCalculator calculator) {
        List<Double> infos = calculator.calculateTravelInfo(startHubId, endHubId);
        this.travelInfo = TravelInfo.builder()
                .duration(infos.get(0))
                .distance(infos.get(1))
                .build();
    }

    /**
     * 허브 간 이동거리와 소요시간을 재계산하여 수정
     * 마스터 관리자만 수정 가능
     * */
    public void changeTravelInfo(TravelInfoCalculator calculator, CheckAccessHub checkAccessHub) {
        checkAccessHub.checkAccess();
        List<Double> infos = calculator.calculateTravelInfo(startHubId, endHubId);
        this.travelInfo = TravelInfo.builder()
                .duration(infos.get(0))
                .distance(infos.get(1))
                .build();
    }

    /**
     * 마스터 관리자만 삭제 가능
     * soft delete 처리
     * */
    public void deleteHubRoute(String username, CheckAccessHub checkAccessHub) {
        checkAccessHub.checkAccess();
        this.softDelete(username);
    }
}
