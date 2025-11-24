package org.pokeherb.hubservice.domain.hub.service;

import java.util.Map;

public interface AddressToCoordinateConverter {
    Map<String, Double> convert(String address);
}
