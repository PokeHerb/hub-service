package org.pokeherb.hubservice.application.hub.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "시/도, 시/군/구, 읍/면, 리, 동, 도로명, 건물 번호, 상세주소로 이루어진 주소 DTO")
public record AddressDto(
        String sido,
        String sigungu,
        String eupmyeon,
        String ri,
        String dong,
        String street,
        String buildingNo,
        String details
) {
}
