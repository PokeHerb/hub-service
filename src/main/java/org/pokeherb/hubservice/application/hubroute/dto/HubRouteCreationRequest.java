package org.pokeherb.hubservice.application.hubroute.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "허브 간 이동 정보 생성 요청 DTO")
public record HubRouteCreationRequest(
        @Schema(description = "출발 허브 id")
        Long startHubId,
        @Schema(description = "도착 허브 id")
        Long endHubId
) {
}
