package org.pokeherb.hubservice.application.hub.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "허브 생성 요청 DTO")
public record HubCreationRequest(
        @Schema(description = "허브 이름", example = "서울특별시 센터")
        String hubName,
        AddressDto address
) {
}
