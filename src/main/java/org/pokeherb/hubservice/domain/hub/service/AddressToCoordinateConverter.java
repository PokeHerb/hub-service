package org.pokeherb.hubservice.domain.hub.service;

import java.util.List;

public interface AddressToCoordinateConverter {
    List<Double> convert(String address);
}
