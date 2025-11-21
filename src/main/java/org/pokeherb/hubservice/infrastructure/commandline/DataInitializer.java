package org.pokeherb.hubservice.infrastructure.commandline;

import lombok.RequiredArgsConstructor;
import org.pokeherb.hubservice.application.hub.command.HubCommandService;
import org.pokeherb.hubservice.application.hub.dto.AddressDto;
import org.pokeherb.hubservice.application.hub.dto.HubCreationRequest;
import org.pokeherb.hubservice.application.hub.dto.HubResponse;
import org.pokeherb.hubservice.application.hubroute.command.HubRouteCommandService;
import org.pokeherb.hubservice.application.hubroute.dto.HubRouteCreationRequest;
import org.pokeherb.hubservice.domain.hub.repository.HubRepository;
import org.pokeherb.hubservice.infrastructure.security.CustomUserDetails;
import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final HubCommandService hubCommandService;
    private final HubRepository hubRepository;
    private final HubRouteCommandService hubRouteCommandService;

    @Override
    public void run(String... args) throws Exception {
        insertData();
    }

    @Async("simpleTaskExecutor")
    public void insertData() {
        UserDetails userDetails = CustomUserDetails.builder()
                .userId(UUID.randomUUID())
                .username("dataInitializer")
                .roles("ROLE_MASTER")
                .build();

        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);

        if (hubRepository.count() > 0) return;

        // 허브 생성
        List<HubCreationRequest> hubRequests = List.of(
                new HubCreationRequest("서울특별시 센터",
                        new AddressDto("서울특별자치시", "송파구", null, null, null, "송파대로", "55", null)),
                new HubCreationRequest("경기북부 센터",
                        new AddressDto("경기도", "고양시 덕양구", null, null, null, "권율대로", "570", null)),
                new HubCreationRequest("경기남부 센터",
                        new AddressDto("경기도", "이천시", null, null, null, "덕평로", "257-21", null)),
                new HubCreationRequest("부산광역시 센터",
                        new AddressDto("부산광역시", "동구", null, null, null, "중앙대로", "206", null)),
                new HubCreationRequest("대구광역시 센터",
                        new AddressDto("대구광역시", "북구", null, null, null, "태평로", "161", null)),
                new HubCreationRequest("인천광역시 센터",
                        new AddressDto("인천광역시", "남동구", null, null, null, "정각로", "29", null)),
                new HubCreationRequest("광주광역시 센터",
                        new AddressDto("광주광역시", "서구", null, null, null, "내방로", "111", null)),
                new HubCreationRequest("대전광역시 센터",
                        new AddressDto("대전광역시", "서구", null, null, null, "둔산로", "100", null)),
                new HubCreationRequest("울산광역시 센터",
                        new AddressDto("울산광역시", "남구", null, null, null, "중앙로", "201", null)),
                new HubCreationRequest("세종특별자치시 센터",
                        new AddressDto("세종특별자치시", null, null, null, null, "한누리대로", "2130", null)),
                new HubCreationRequest("강원특별자치도 센터",
                        new AddressDto("강원특별자치도", "춘천시", null, null, null, "중앙로", "1", null)),
                new HubCreationRequest("충청북도 센터",
                        new AddressDto("충청북도", "청주시 상당구", null, null, null, "상당로", "82", null)),
                new HubCreationRequest("충청남도 센터",
                        new AddressDto("충청남도", "홍성군 홍북읍", null, null, null, "충남대로", "21", null)),
                new HubCreationRequest("전북특별자치도 센터",
                        new AddressDto("전북특별자치도", "전주시 완산구", null, null, null, "효자로", "225", null)),
                new HubCreationRequest("전라남도 센터",
                        new AddressDto("전라남도", "무안군 삼향읍", null, null, null, "오룡길", "1", null)),
                new HubCreationRequest("경상북도 센터",
                        new AddressDto("경상북도", "안동시 풍천면", null, null, null, "도청대로", "455", null)),
                new HubCreationRequest("경상남도 센터",
                        new AddressDto("경상남도", "창원시 의창구", null, null, null, "중앙대로", "300", null))
        );

        // 서비스 호출로 허브 생성
        Map<String, Long> hubMap = new HashMap<>();
        for (HubCreationRequest req : hubRequests) {
            HubResponse hub = hubCommandService.createHub(req);
            hubMap.put(hub.hubName(), hub.hubId());
        }

        Map<String, List<String>> connections = Map.of(
                "경기남부 센터", List.of("경기북부 센터", "서울특별시 센터", "인천광역시 센터", "강원특별자치도 센터", "경상북도 센터", "대전광역시 센터", "대구광역시 센터"),
                "대전광역시 센터", List.of("충청남도 센터", "충청북도 센터", "세종특별자치시 센터", "전북특별자치도 센터", "광주광역시 센터", "전라남도 센터", "경기남부 센터", "대구광역시 센터"),
                "대구광역시 센터", List.of("경상북도 센터", "경상남도 센터", "부산광역시 센터", "울산광역시 센터", "경기남부 센터", "대전광역시 센터"),
                "경상북도 센터", List.of("경기남부 센터", "대구광역시 센터")
        );

        Set<String> addedRoutes = new HashSet<>();

        for (Map.Entry<String, List<String>> entry : connections.entrySet()) {
            String startHubName = entry.getKey();
            Long startHubId = hubMap.get(startHubName);
            if (startHubId == null) continue;

            for (String endName : entry.getValue()) {
                Long endHubId = hubMap.get(endName);
                if (endHubId == null) continue;

                String key1 = startHubName + "->" + endName;
                String key2 = endName + "->" + startHubName;

                if (!addedRoutes.contains(key1) && !addedRoutes.contains(key2)) {

                    // 단방향
                    hubRouteCommandService.createHubRoute(
                            new HubRouteCreationRequest(startHubId, endHubId)
                    );

                    // 반대 방향
                    hubRouteCommandService.createHubRoute(
                            new HubRouteCreationRequest(endHubId, startHubId)
                    );

                    addedRoutes.add(key1);
                    addedRoutes.add(key2);
                }
            }
        }
    }
}
