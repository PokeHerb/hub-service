package org.pokeherb.hubservice.infrastructure.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients("org.pokeherb.hubservice")
public class FeignConfig {
}
