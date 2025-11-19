package org.pokeherb.hubservice.application.hub.dto;

public record HubModificationRequest(
        String hubName,
        AddressDto address
) {
}
