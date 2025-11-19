package org.pokeherb.hubservice;

import lombok.RequiredArgsConstructor;
import org.pokeherb.hubservice.domain.hub.entity.Hub;
import org.pokeherb.hubservice.domain.hub.repository.HubRepository;
import org.pokeherb.hubservice.domain.hub.service.AddressToCoordinateConverter;
import org.pokeherb.hubservice.domain.hub.service.CheckAccessHub;
import org.pokeherb.hubservice.domain.hubroute.entity.HubRoute;
import org.pokeherb.hubservice.domain.hubroute.repository.HubRouteRepository;
import org.pokeherb.hubservice.domain.hubroute.service.TravelInfoCalculator;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final HubRepository hubRepository;
    private final HubRouteRepository hubRouteRepository;
    private final CheckAccessHub checkAccessHub;
    private final AddressToCoordinateConverter addressConverter;
    private final TravelInfoCalculator travelInfoCalculator;

    @Override
    public void run(String... args) throws Exception {
        if (hubRepository.count() > 0) return;

        // 허브 생성
        List<Hub> hubs = List.of(
                Hub.builder().hubName("서울특별시 센터").sido("서울특별시").sigungu("송파구").street("송파대로").building_no("55").checkAccessHub(checkAccessHub).converter(addressConverter).build(),
                Hub.builder().hubName("경기북부 센터").sido("경기도").sigungu("고양시 덕양구").street("권율대로").building_no("570").checkAccessHub(checkAccessHub).converter(addressConverter).build(),
                Hub.builder().hubName("경기남부 센터").sido("경기도").sigungu("이천시").street("덕평로").building_no("257-21").checkAccessHub(checkAccessHub).converter(addressConverter).build(),
                Hub.builder().hubName("부산광역시 센터").sido("부산광역시").sigungu("동구").street("중앙대로").building_no("206").checkAccessHub(checkAccessHub).converter(addressConverter).build(),
                Hub.builder().hubName("대구광역시 센터").sido("대구광역시").sigungu("북구").street("태평로").building_no("161").checkAccessHub(checkAccessHub).converter(addressConverter).build(),
                Hub.builder().hubName("인천광역시 센터").sido("인천광역시").sigungu("남동구").street("정각로").building_no("29").checkAccessHub(checkAccessHub).converter(addressConverter).build(),
                Hub.builder().hubName("광주광역시 센터").sido("광주광역시").sigungu("서구").street("내방로").building_no("111").checkAccessHub(checkAccessHub).converter(addressConverter).build(),
                Hub.builder().hubName("대전광역시 센터").sido("대전광역시").sigungu("서구").street("둔산로").building_no("100").checkAccessHub(checkAccessHub).converter(addressConverter).build(),
                Hub.builder().hubName("울산광역시 센터").sido("울산광역시").sigungu("남구").street("중앙로").building_no("201").checkAccessHub(checkAccessHub).converter(addressConverter).build(),
                Hub.builder().hubName("세종특별자치시 센터").sido("세종특별자치시").street("한누리대로").building_no("2130").checkAccessHub(checkAccessHub).converter(addressConverter).build(),
                Hub.builder().hubName("강원특별자치도 센터").sido("강원특별자치도").sigungu("춘천시").street("중앙로").building_no("1").checkAccessHub(checkAccessHub).converter(addressConverter).build(),
                Hub.builder().hubName("충청북도 센터").sido("충청북도").sigungu("청주시 상당구").street("상당로").building_no("82").checkAccessHub(checkAccessHub).converter(addressConverter).build(),
                Hub.builder().hubName("충청남도 센터").sido("충청남도").sigungu("홍성군 홍북읍").street("충남대로").building_no("21").checkAccessHub(checkAccessHub).converter(addressConverter).build(),
                Hub.builder().hubName("전북특별자치도 센터").sido("전북특별자치도").sigungu("전주시 완산구").street("효자로").building_no("225").checkAccessHub(checkAccessHub).converter(addressConverter).build(),
                Hub.builder().hubName("전라남도 센터").sido("전라남도").sigungu("무안군 삼향읍").street("오룡길").building_no("1").checkAccessHub(checkAccessHub).converter(addressConverter).build(),
                Hub.builder().hubName("경상북도 센터").sido("경상북도").sigungu("안동시 풍천면").street("도청대로").building_no("455").checkAccessHub(checkAccessHub).converter(addressConverter).build(),
                Hub.builder().hubName("경상남도 센터").sido("경상남도").sigungu("창원시 의창구").street("중앙대로").building_no("300").checkAccessHub(checkAccessHub).converter(addressConverter).build()
        );

        hubRepository.saveAll(hubs);

        // 허브 이름으로 조회
        Map<String, Hub> hubMap = hubs.stream().collect(Collectors.toMap(Hub::getHubName, h -> h));

        List<HubRoute> routes = new ArrayList<>();

        // 연결 정보 정의 (start -> List<end>)
        Map<String, List<String>> connections = Map.of(
                "경기남부 센터", List.of("경기북부 센터", "서울특별시 센터", "인천광역시 센터", "강원특별자치도 센터", "경상북도 센터", "대전광역시 센터", "대구광역시 센터"),
                "대전광역시 센터", List.of("충청남도 센터", "충청북도 센터", "세종특별자치시 센터", "전북특별자치도 센터", "광주광역시 센터", "전라남도 센터", "경기남부 센터", "대구광역시 센터"),
                "대구광역시 센터", List.of("경상북도 센터", "경상남도 센터", "부산광역시 센터", "울산광역시 센터", "경기남부 센터", "대전광역시 센터"),
                "경상북도 센터", List.of("경기남부 센터", "대구광역시 센터")
        );

        // HubRoute 생성 (양방향)
        for (Map.Entry<String, List<String>> entry : connections.entrySet()) {
            Hub start = hubMap.get(entry.getKey());
            for (String endName : entry.getValue()) {
                Hub end = hubMap.get(endName);
                if (start != null && end != null) {
                    routes.add(createHubRoute(start, end));
                    routes.add(createHubRoute(end, start)); // 양방향
                }
            }
        }

        hubRouteRepository.saveAll(routes);
    }

    private HubRoute createHubRoute(Hub start, Hub end) {
        return HubRoute.builder()
                .startHubId(start.getHubId())
                .endHubId(end.getHubId())
                .checkAccessHub(checkAccessHub)
                .calculator(travelInfoCalculator)
                .build();
    }
}
