package org.pokeherb.hubservice.presentation.hubroute;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.pokeherb.hubservice.application.hub.dto.HubResponse;
import org.pokeherb.hubservice.application.hubroute.command.HubRouteCommandService;
import org.pokeherb.hubservice.application.hubroute.dto.HubRouteCreationRequest;
import org.pokeherb.hubservice.application.hubroute.dto.HubRouteResponse;
import org.pokeherb.hubservice.application.finalroute.service.FinalRouteService;
import org.pokeherb.hubservice.application.hubroute.query.HubRouteQueryService;
import org.pokeherb.hubservice.global.infrastructure.CustomResponse;
import org.pokeherb.hubservice.global.infrastructure.success.GeneralSuccessCode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "HubRoute API", description = "허브 간 이동 정보 생성/조회/수정/삭제 및 검색을 위한 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/route")
public class HubRouteController {

    private final HubRouteCommandService hubRouteCommandService;
    private final HubRouteQueryService hubRouteQueryService;
    private final FinalRouteService finalRouteService;

    @Operation(summary = "출발 허브에서 목적 허브까지 도달하는 최종 이동 경로 조회")
    @GetMapping("/final/{startHubId}/{endHubId}")
    public CustomResponse<List<HubResponse>> getFinalHubRouteSequence(@PathVariable Long startHubId, @PathVariable Long endHubId, @RequestParam("cost") String cost) {
        List<HubResponse> routeSequence = finalRouteService.getFinalHubRoute(startHubId, endHubId, cost);
        return CustomResponse.onSuccess(GeneralSuccessCode.OK, routeSequence);
    }

    @Operation(summary = "허브 간 이동 정보 생성")
    @PostMapping
    public CustomResponse<HubRouteResponse> createHubRoute(@Valid @RequestBody HubRouteCreationRequest request) {
        HubRouteResponse hubRouteResponse = hubRouteCommandService.createHubRoute(request);
        return CustomResponse.onSuccess(GeneralSuccessCode.CREATED, hubRouteResponse);
    }

    @Operation(summary = "출발 허브에서 도착 허브까지의 소요시간 및 이동거리 조회")
    @GetMapping("/{startHudId}/{endHubId}")
    public CustomResponse<HubRouteResponse> getHubRoute(@PathVariable Long startHudId,
            @PathVariable Long endHubId) {
        HubRouteResponse hubRouteResponse = hubRouteQueryService.getHubRoute(startHudId, endHubId);
        return CustomResponse.onSuccess(GeneralSuccessCode.OK, hubRouteResponse);
    }

    @Operation(summary = "모든 허브 간 이동 정보 목록 조회")
    @GetMapping
    public CustomResponse<Page<HubRouteResponse>> getHubRouteList(Pageable pageable) {
        Page<HubRouteResponse> hubRouteResponses = hubRouteQueryService.getHubRouteList(pageable);
        return CustomResponse.onSuccess(GeneralSuccessCode.OK, hubRouteResponses);
    }

    @Operation(summary = "허브 간 이동 정보 업데이트")
    @PatchMapping("/{startHudId}/{endHubId}")
    public CustomResponse<HubRouteResponse> updateHubRoute(@PathVariable Long startHudId, @PathVariable Long endHubId) {
        HubRouteResponse hubRouteResponse = hubRouteCommandService.updateHubRoute(startHudId, endHubId);
        return CustomResponse.onSuccess(GeneralSuccessCode.OK, hubRouteResponse);
    }

    @Operation(summary = "허브 간 이동 정보 삭제")
    @DeleteMapping("/{startHudId}/{endHubId}")
    public CustomResponse<?> deleteHubRoute(@PathVariable Long startHudId, @PathVariable Long endHubId) {
        hubRouteCommandService.deleteHubRoute(startHudId, endHubId);
        return CustomResponse.onSuccess(GeneralSuccessCode.DELETED, null);
    }

    @Operation(summary = "허브 간 이동 정보 검색")
    @GetMapping("/search")
    public CustomResponse<Page<HubRouteResponse>> searchHubRoute(@RequestParam("keyword") String keyword, Pageable pageable) {
        Page<HubRouteResponse> hubRouteResponses = hubRouteQueryService.searchHubRouteList(keyword, pageable);
        return CustomResponse.onSuccess(GeneralSuccessCode.OK, hubRouteResponses);
    }
}
