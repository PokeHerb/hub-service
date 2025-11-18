package org.pokeherb.hubservice.domain.routing.value;

import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FinalTravelInfo {
    // 최종 소요시간
    private Double totalTravelTimeMin;
    // 최종 이동거리
    private Double totalDistance;

    @Builder
    public FinalTravelInfo(Double totalTravelTimeMin, Double totalDistance) {
        this.totalTravelTimeMin = totalTravelTimeMin;
        this.totalDistance = totalDistance;
    }
}
