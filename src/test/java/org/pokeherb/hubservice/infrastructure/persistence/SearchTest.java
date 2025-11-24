package org.pokeherb.hubservice.infrastructure.persistence;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.pokeherb.hubservice.domain.hub.entity.Hub;
import org.pokeherb.hubservice.domain.hubroute.entity.HubRoute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@SpringBootTest
class SearchTest {

    @Autowired
    private HubRouteDetailsDao dao;

    @Autowired
    private HubDetailsDao hubDetailsDao;

    @Test
    @DisplayName(value = "키워드로 검색 성공")
    void searchHubRouteByKeyword_success() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<HubRoute> result = dao.searchHubRouteByKeyword("서울", pageable);

        assertThat(result).isNotEmpty();
        System.out.println(result);
        System.out.println(result.getTotalElements());
    }

    @Test
    @DisplayName(value = "검색 결과 반환값 없음")
    void searchHubRouteByKeyword_noResult() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<HubRoute> result = dao.searchHubRouteByKeyword("아무것도없음", pageable);

        assertThat(result).isEmpty();
        System.out.println(result);
    }

    @Test
    @DisplayName(value = "허브 검색")
    void searchHubByKeywordTest() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Hub> result = hubDetailsDao.searchHubByKeyword("서울", pageable);
        assertThat(result).isNotEmpty();
        System.out.println(result.getContent());
    }
}