package org.pokeherb.hubservice.application.hub.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "허브 정보 수정 요청 DTO")
public record HubModificationRequest(
        @Schema(description = "허브 이름", example = "부산광역시 센터")
        String hubName,
        AddressDto address
) {
}
