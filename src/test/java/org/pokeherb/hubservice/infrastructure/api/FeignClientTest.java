package org.pokeherb.hubservice.infrastructure.api;

import org.junit.jupiter.api.Test;
import org.pokeherb.hubservice.global.infrastructure.CustomResponse;
import org.pokeherb.hubservice.infrastructure.api.dto.DriverIdResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class FeignClientTest {

    @Autowired
    DriverServiceClient driverServiceClient;

    @Test
    void test() {
        CustomResponse<DriverIdResponse> res = driverServiceClient.getHubDriverId();
        System.out.println(res.getResult());
    }
}
