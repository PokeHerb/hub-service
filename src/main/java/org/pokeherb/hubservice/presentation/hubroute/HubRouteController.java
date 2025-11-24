package org.pokeherb.hubservice.presentation.hubroute;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.pokeherb.hubservice.application.hub.dto.HubResponse;
import org.pokeherb.hubservice.application.hubroute.command.HubRouteCommandService;
import org.pokeherb.hubservice.application.hubroute.dto.HubRouteCreationRequest;
import org.pokeherb.hubservice.application.hubroute.dto.HubRouteResponse;
import org.pokeherb.hubservice.application.hubroute.query.FinalHubRouteQueryService;
import org.pokeherb.hubservice.application.hubroute.query.HubRouteQueryService;
import org.pokeherb.hubservice.global.infrastructure.CustomResponse;
import org.pokeherb.hubservice.global.infrastructure.success.GeneralSuccessCode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/routing")
public class HubRouteController {

    private final HubRouteCommandService hubRouteCommandService;
    private final HubRouteQueryService hubRouteQueryService;
    private final FinalHubRouteQueryService finalHubRouteQueryService;

    @GetMapping("/final/{startHubId}/{endHubId}")
    public CustomResponse<List<HubResponse>> getFinalHubRouteSequence(@PathVariable Long startHubId, @PathVariable Long endHubId) {
        List<HubResponse> routeSequence = finalHubRouteQueryService.getFinalHubRoute(startHubId, endHubId);
        return CustomResponse.onSuccess(GeneralSuccessCode.OK, routeSequence);
    }

    @PostMapping
    public CustomResponse<HubRouteResponse> createHubRoute(@Valid @RequestBody HubRouteCreationRequest request) {
        HubRouteResponse hubRouteResponse = hubRouteCommandService.createHubRoute(request);
        return CustomResponse.onSuccess(GeneralSuccessCode.CREATED, hubRouteResponse);
    }

    @GetMapping("/{startHudId}/{endHubId}")
    public CustomResponse<HubRouteResponse> getHubRoute(@PathVariable Long startHudId,
            @PathVariable Long endHubId) {
        HubRouteResponse hubRouteResponse = hubRouteQueryService.getHubRoute(startHudId, endHubId);
        return CustomResponse.onSuccess(GeneralSuccessCode.OK, hubRouteResponse);
    }

    @GetMapping
    public CustomResponse<Page<HubRouteResponse>> getHubRouteList(Pageable pageable) {
        Page<HubRouteResponse> hubRouteResponses = hubRouteQueryService.getHubRouteList(pageable);
        return CustomResponse.onSuccess(GeneralSuccessCode.OK, hubRouteResponses);
    }

    @PatchMapping("/{startHudId}/{endHubId}")
    public CustomResponse<HubRouteResponse> updateHubRoute(@PathVariable Long startHudId, @PathVariable Long endHubId) {
        HubRouteResponse hubRouteResponse = hubRouteCommandService.updateHubRoute(startHudId, endHubId);
        return CustomResponse.onSuccess(GeneralSuccessCode.OK, hubRouteResponse);
    }

    @DeleteMapping("/{startHudId}/{endHubId}")
    public CustomResponse<?> deleteHubRoute(@PathVariable Long startHudId, @PathVariable Long endHubId) {
        hubRouteCommandService.deleteHubRoute(startHudId, endHubId);
        return CustomResponse.onSuccess(GeneralSuccessCode.DELETED, null);
    }

    @GetMapping("/search")
    public CustomResponse<List<HubRouteResponse>> searchHubRoute(@RequestParam("keyword") String keyword) {
        List<HubRouteResponse> hubRouteResponses = hubRouteQueryService.searchHubRouteList(keyword);
        return CustomResponse.onSuccess(GeneralSuccessCode.OK, hubRouteResponses);
    }
}
