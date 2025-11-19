package org.pokeherb.hubservice.application.hub.dto;

public record HubCreationRequest(
        String hubName,
        AddressDto address
) {
}
