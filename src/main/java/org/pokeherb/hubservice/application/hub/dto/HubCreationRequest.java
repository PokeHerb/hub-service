package org.pokeherb.hubservice.application.hub.dto;

public record HubCreationRequest(
        String hubName,
        String sido,
        String sigungu,
        String eupmyeon,
        String ri,
        String dong,
        String street,
        String buildingNo
) {
}
