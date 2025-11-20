package org.pokeherb.hubservice.infrastructure.persistence;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.pokeherb.hubservice.domain.hubroute.entity.HubRoute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@SpringBootTest
class HubRouteDetailsDaoTest {

    @Autowired
    private HubRouteDetailsDao dao;

    @Test
    @DisplayName(value = "키워드로 검색 성공")
    void searchHubRouteByKeyword_success() {
        List<HubRoute> result = dao.searchHubRouteByKeyword("서울");

        assertThat(result).isNotEmpty();
        assertThat(result.get(0).getStartHubId()).isNotNull();
        System.out.println(result);
    }

    @Test
    @DisplayName(value = "검색 결과 반환값 없음")
    void searchHubRouteByKeyword_noResult() {
        List<HubRoute> result = dao.searchHubRouteByKeyword("아무것도없음");

        assertThat(result).isEmpty();
        System.out.println(result);
    }
}