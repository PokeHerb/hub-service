package org.pokeherb.hubservice.domain.routing;

import jakarta.persistence.*;
import lombok.*;
import org.pokeherb.hubservice.domain.hub.CheckAccessHub;
import org.pokeherb.hubservice.global.domain.Auditable;

import java.util.List;
import java.util.UUID;

/**
 * 1. 허브 간 최종 이동 정보는 모든 사용자가 조회 가능
 * 2. 수정, 삭제는 마스터 관리자만 가능
 * 3. 허브, 허브 간 이동 정보 segment가 생성된 후 종속적으로 생성
 * 4. 소요시간, 이동거리는 경로 탐색 앱을 활용해 미리 조회 및 저장
 * */
@Entity
@Table(name = "p_fianl_routing")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Access(AccessType.FIELD)
@ToString
public class FinalRouting extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID finalRoutingId;

    private UUID startHudId;

    private UUID endHubId;

    @Embedded
    private FinalTravelInfo finalTravelInfo;

    private List<UUID> routeSequence;

    /**
     * 허브 간 최종 이동 경로는 허브, 허브 간 이동 경로 segment가 생성된 후 종속적으로 생성
     * */
    @Builder
    public FinalRouting(UUID startHudId, UUID endHubId, FinalRouteFactory factory) {
        this.startHudId = startHudId;
        this.endHubId = endHubId;
        setRouteSequence(factory);
    }

    /**
     * 최종 이동 경로 결정
     * */
    private void setRouteSequence(FinalRouteFactory finalRouteFactory) {
        this.routeSequence = finalRouteFactory.getRouteSequence(startHudId, endHubId);
    }

    /**
     * 최종 이동 경로에 따른 최종 소요시간 및 이동거리 계산
     * */
    private void setFinalTravelInfo(UUID startHudId, UUID endHubId, TravelInfoCalculator calculator) {
        List<Double> infos = calculator.calculateTravelInfo(startHudId, endHubId, routeSequence);
        this.finalTravelInfo = FinalTravelInfo.builder()
                .totalTravelTimeMin(infos.get(0))
                .totalDistance(infos.get(1))
                .build();
    }

    /**
     * 허브 간 최종 이동 경로 재계산
     * 마스터 관리자만 가능
     * */
    public void changeRouteSequence(FinalRouteFactory finalRouteFactory, CheckAccessHub checkAccessHub) {
        checkAccessHub.checkAccess();
        setRouteSequence(finalRouteFactory);
    }

    /**
     * 허브 간 최종 이동 경로에 따른 소요시간 및 이동거리 재계산
     * 마스터 관리자만 가능
     * */
    public void changeFinalTravelInfo(TravelInfoCalculator calculator, CheckAccessHub checkAccessHub) {
        checkAccessHub.checkAccess();
        calculator.calculateTravelInfo(startHudId, endHubId, routeSequence);
    }
}
