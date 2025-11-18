package org.pokeherb.hubservice.domain.hub;

import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Coordinate {
    // 위도
    private Double latitude;
    // 경도
    private Double longitude;

    @Builder
    public Coordinate(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
