package org.pokeherb.hubservice.domain.hub;

import java.util.List;

public interface AddressToCoordinateConverter {
    List<Double> convert(String address);
}
