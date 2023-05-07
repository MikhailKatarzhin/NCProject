package ncp.model;

import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;


@Getter
@Setter
@RequiredArgsConstructor
@Table
@Entity
public class Transmitter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;

    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private TransmitterStatus status;

    @ManyToMany
    @JoinTable(name = "transmitter_available_addresses",
            joinColumns = @JoinColumn(name = "transmitter_id"),
            inverseJoinColumns = @JoinColumn(name = "available_addresses_id"))
    private Set<Address> availableAddresses = new HashSet<>();

    @ManyToOne
    private Address address;

    @ManyToMany
    @JoinTable(name = "tariff_connected_transmitters",
            joinColumns = @JoinColumn(name = "connected_transmitters_id"),
            inverseJoinColumns = @JoinColumn(name = "tariff_id"))
    private Set<Tariff> connectedTariffs = new HashSet<>();
}
