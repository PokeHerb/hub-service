package org.pokeherb.hubservice.infrastructure.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pokeherb.hubservice.application.finalroute.dto.FinalRouteResponse;
import org.pokeherb.hubservice.application.finalroute.service.FinalRouteService;
import org.pokeherb.hubservice.application.hub.dto.HubResponse;
import org.pokeherb.hubservice.domain.hub.service.AddressToCoordinateConverter;
import org.pokeherb.hubservice.infrastructure.messaging.dto.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class RabbitMessageEventHandler implements MessageEventHandler {

    private final ObjectMapper objectMapper;
    private final AddressToCoordinateConverter addressToCoordinateConverter;
    private final FinalRouteService finalRouteService;
    private final RabbitProducer rabbitProducer;
    //private final DriverServiceClient driverServiceClient;


    @Override
    public void handleOrderCreatedEvent(String payload) throws JsonProcessingException {
        // 주문 생성 메시지 payload 받기
        OrderCreatedEventMessage receivedMessage = objectMapper.readValue(payload, OrderCreatedEventMessage.class);
        log.info("Received OrderCreatedEventMessage from RabbitMQ: {}", receivedMessage);
        // 업체 주소를 좌표로 변환
        Map<String, Double> coordinate = addressToCoordinateConverter.convert(receivedMessage.vendorAddress());
        log.info("Received coordinate from RabbitMQ: {}", coordinate);
        // 출발 허브와 목적 허브를 이용하여 최종 경로 계산
        List<HubResponse> route = finalRouteService.getFinalHubRoute(receivedMessage.startHubId(), receivedMessage.endHubId(), "duration");
        log.info("Received route from RabbitMQ: {}", route);
        // 최종 경로와 업체 주소(목적지)를 바탕으로 최종 이동거리 및 소요시간 계산
        FinalRouteResponse finalRouteResponse = finalRouteService.getDeliveryInfo(route, coordinate);
        log.info("Received finalRoute from RabbitMQ: {}", finalRouteResponse);
        List<Long> routeSequence = route.stream().map(HubResponse::hubId).toList();

        //List<Long> routeSequence = List.of(1L, 3L, 5L, 4L);
        log.info("Received routeSequence from RabbitMQ: {}", routeSequence);
        // 삼품 재고 감소 요청 메시지 발행
        ProductStockRequestMessage productStockRequestMessage = new ProductStockRequestMessage(receivedMessage.productId(), receivedMessage.quantity());
        log.info("Product stock request message {}", productStockRequestMessage);
        rabbitProducer.publishEvent(productStockRequestMessage, "product.decrease.stock"); // TODO: 재고 감소 라우팅키 확인
        // 허브 배송 담당자 배정 확인
        //if (driverServiceClient.getHubDriverId().getResult() == null) {
        //    throw new CustomException(ApiErrorCode.FAIL_ASSIGN_HUB_DRIVER);
        //}
        // 업체 배송 담당자 배정 확인
        //CustomResponse<DriverIdResponse> res = driverServiceClient.getVendorDriverId(receivedMessage.endHubId());
        //if (res.getResult() == null) {
        //    throw new CustomException(ApiErrorCode.FAIL_ASSIGN_VENDOR_DRIVER);
        //}
        //UUID driverId = res.getResult().driverId();
        UUID driverId = UUID.randomUUID();
        // 배송 생성 요청 메시지 발행
        DeliveryCreateRequestMessage deliveryCreateRequestMessage = DeliveryCreateRequestMessage.from(receivedMessage, routeSequence, finalRouteResponse.finalDuration(), finalRouteResponse.finalDistance(), driverId);
        log.info("Received DeliveryCreateRequestMessage from RabbitMQ: {}", deliveryCreateRequestMessage);
        rabbitProducer.publishEvent(deliveryCreateRequestMessage, "delivery.create");
        // 주문 상태 변경 요청 메시지 발행 (배송 시작)
        //OrderStatusRequestMessage orderStatusRequestMessage = new OrderStatusRequestMessage(receivedMessage.orderUserId(), "배송 시작");
        //rabbitProducer.publishEvent(orderStatusRequestMessage, "order.status");
    }

    @Override
    public void handleOrderCanceledEvent(String payload) throws JsonProcessingException {
        OrderCanceledEventMessage receiveMessage = objectMapper.readValue(payload, OrderCanceledEventMessage.class);
        ProductStockRequestMessage productStockRequestMessage = new ProductStockRequestMessage(receiveMessage.productId(), receiveMessage.stock());
        rabbitProducer.publishEvent(productStockRequestMessage, "product.increase.stock"); // TODO : 재고 증가 라우팅키 확인
    }
}
