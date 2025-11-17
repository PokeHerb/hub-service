package org.pokeherb.hubservice.domain.hub;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.pokeherb.hubservice.global.domain.Auditable;

import java.util.UUID;

@Entity
@Table(name = "p_hub")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Access(AccessType.FIELD)
@ToString
public class Hub extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID hub_id;

    private String hub_name;

    @Embedded
    private Address address;

    private Double latitude;

    private Double longitude;
}
