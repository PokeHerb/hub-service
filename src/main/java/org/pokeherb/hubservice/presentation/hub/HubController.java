package org.pokeherb.hubservice.presentation.hub;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.pokeherb.hubservice.application.hub.command.HubCommandService;
import org.pokeherb.hubservice.application.hub.dto.HubCreationRequest;
import org.pokeherb.hubservice.application.hub.dto.HubModificationRequest;
import org.pokeherb.hubservice.application.hub.dto.HubResponse;
import org.pokeherb.hubservice.application.hub.query.HubQueryService;
import org.pokeherb.hubservice.global.infrastructure.CustomResponse;
import org.pokeherb.hubservice.global.infrastructure.success.GeneralSuccessCode;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/hub")
public class HubController {

    private final HubCommandService hubCommandService;
    private final HubQueryService hubQueryService;

    @PostMapping
    public CustomResponse<HubResponse> createHub(@Valid @RequestBody HubCreationRequest request) {
        HubResponse hubResponse = hubCommandService.createHub(request);
        return CustomResponse.onSuccess(GeneralSuccessCode.CREATED, hubResponse);
    }

    @GetMapping
    public CustomResponse<List<HubResponse>> getHubList() {
        List<HubResponse> hubResponses = hubQueryService.getHubList();
        return CustomResponse.onSuccess(GeneralSuccessCode.OK, hubResponses);
    }

    @GetMapping("/{hubId}")
    public CustomResponse<HubResponse> getHub(@PathVariable Long hubId) {
        HubResponse hubResponse = hubQueryService.getHub(hubId);
        return CustomResponse.onSuccess(GeneralSuccessCode.OK, hubResponse);
    }

    @PatchMapping("/{hubId}")
    public CustomResponse<HubResponse> updateHub(@PathVariable Long hubId, @Valid @RequestBody HubModificationRequest request) {
        HubResponse hubResponse = hubCommandService.modifyHub(hubId, request);
        return CustomResponse.onSuccess(GeneralSuccessCode.OK, hubResponse);
    }

    @DeleteMapping("/{hubId}")
    public CustomResponse<?> deleteHub(@PathVariable Long hubId) {
        hubCommandService.deleteHub(hubId);
        return CustomResponse.onSuccess(GeneralSuccessCode.DELETED, null);
    }

    @GetMapping("/search")
    public CustomResponse<List<HubResponse>> searchHub(@RequestParam("keyword") String keyword) {
        List<HubResponse> hubResponses = hubQueryService.searchHubList(keyword);
        return CustomResponse.onSuccess(GeneralSuccessCode.OK, hubResponses);
    }
}
