package org.pokeherb.hubservice.application.finalroute.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import org.pokeherb.hubservice.application.hub.dto.HubResponse;

import java.util.List;

@Schema(description = "최종 경로 및 소요시간, 이동거리 반환을 위한 DTO")
public record FinalRouteResponse(
        @Schema(description = "최종 이동거리(m)")
        Double finalDistance,
        @Schema(description = "최종 소요시간(s)")
        Double finalDuration,
        List<HubResponse> routeSequence
) {
}
