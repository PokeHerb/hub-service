package org.pokeherb.hubservice.domain.hubroute.value;

import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TravelInfo {
    // 소요시간
    private Double travelTimeMin;
    // 이동거리
    private Double distance;

    @Builder
    public TravelInfo(Double travelTimeMin, Double distance) {
        this.travelTimeMin = travelTimeMin;
        this.distance = distance;
    }
}
