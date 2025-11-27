package org.pokeherb.hubservice.infrastructure.api;

import org.pokeherb.hubservice.global.infrastructure.CustomResponse;
import org.pokeherb.hubservice.infrastructure.api.dto.DriverIdResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient("driver-service")
public interface DriverServiceClient {
    @GetMapping("/hub")
    CustomResponse<DriverIdResponse> getHubDriverId();

    @GetMapping("/vendor/{hubId}/{orderId}")
    CustomResponse<DriverIdResponse> getVendorDriverId(@PathVariable Long hubId, @PathVariable UUID orderId);
}
