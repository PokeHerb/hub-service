package org.pokeherb.hubservice.domain.routing;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.pokeherb.hubservice.global.domain.Auditable;

import java.util.UUID;

@Entity
@Table(name = "p_routing_segment")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Access(AccessType.FIELD)
@ToString
public class RoutingSegment extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID routingId;

    private UUID startHubId;

    private UUID endHubId;

    private Double travelTimeMin;

    private Double distance;
}
