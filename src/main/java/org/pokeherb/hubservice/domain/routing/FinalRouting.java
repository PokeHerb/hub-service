package org.pokeherb.hubservice.domain.routing;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.pokeherb.hubservice.global.domain.Auditable;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "p_fianl_routing")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Access(AccessType.FIELD)
@ToString
public class FinalRouting extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID finalRoutingId;

    private UUID startHudId;

    private UUID endHubId;

    private Double totalTravelTimeMin;

    private Double totalDistance;

    private List<UUID> routeSequence;
}
