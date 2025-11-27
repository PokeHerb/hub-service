package org.pokeherb.hubservice.domain.hubroute.value;

import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TravelInfo {
    // 소요시간
    private Double duration;
    // 이동거리
    private Double distance;

    @Builder
    public TravelInfo(Double duration, Double distance) {
        this.duration = duration;
        this.distance = distance;
    }
}
