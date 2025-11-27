package org.pokeherb.hubservice.presentation.hub;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.pokeherb.hubservice.application.hub.command.HubCommandService;
import org.pokeherb.hubservice.application.hub.dto.HubCreationRequest;
import org.pokeherb.hubservice.application.hub.dto.HubModificationRequest;
import org.pokeherb.hubservice.application.hub.dto.HubResponse;
import org.pokeherb.hubservice.application.hub.query.HubQueryService;
import org.pokeherb.hubservice.global.infrastructure.CustomResponse;
import org.pokeherb.hubservice.global.infrastructure.success.GeneralSuccessCode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Hub API", description = "허브 생성/조회/수정/삭제 및 검색이 가능한 API")
@RestController
@RequiredArgsConstructor
public class HubController {

    private final HubCommandService hubCommandService;
    private final HubQueryService hubQueryService;

    @Operation(summary = "신규 허브 등록")
    @PostMapping
    public CustomResponse<HubResponse> createHub(@Valid @RequestBody HubCreationRequest request) {
        HubResponse hubResponse = hubCommandService.createHub(request);
        return CustomResponse.onSuccess(GeneralSuccessCode.CREATED, hubResponse);
    }

    @Operation(summary = "모든 허브 목록 조회")
    @GetMapping
    public CustomResponse<Page<HubResponse>> getHubList(Pageable pageable) {
        Page<HubResponse> hubResponses = hubQueryService.getHubList(pageable);
        return CustomResponse.onSuccess(GeneralSuccessCode.OK, hubResponses);
    }

    @Operation(summary = "단일 허브 조회")
    @GetMapping("/{hubId}")
    public CustomResponse<HubResponse> getHub(@PathVariable Long hubId) {
        HubResponse hubResponse = hubQueryService.getHub(hubId);
        return CustomResponse.onSuccess(GeneralSuccessCode.OK, hubResponse);
    }

    @Operation(summary = "허브 정보 수정")
    @PatchMapping("/{hubId}")
    public CustomResponse<HubResponse> updateHub(@PathVariable Long hubId, @Valid @RequestBody HubModificationRequest request) {
        HubResponse hubResponse = hubCommandService.modifyHub(hubId, request);
        return CustomResponse.onSuccess(GeneralSuccessCode.OK, hubResponse);
    }

    @Operation(summary = "허브 삭제")
    @DeleteMapping("/{hubId}")
    public CustomResponse<?> deleteHub(@PathVariable Long hubId) {
        hubCommandService.deleteHub(hubId);
        return CustomResponse.onSuccess(GeneralSuccessCode.DELETED, null);
    }

    @Operation(summary = "허브 검색")
    @GetMapping("/search")
    public CustomResponse<Page<HubResponse>> searchHub(@RequestParam("keyword") String keyword, Pageable pageable) {
        Page<HubResponse> hubResponses = hubQueryService.searchHubList(keyword, pageable);
        return CustomResponse.onSuccess(GeneralSuccessCode.OK, hubResponses);
    }

    @Operation(summary = "허브 존재 여부 확인")
    @GetMapping("/{hubId}/exists")
    public CustomResponse<Boolean> existHub(@PathVariable Long hubId) {
        return CustomResponse.onSuccess(GeneralSuccessCode.OK, hubQueryService.existsHub(hubId));
    }
}
