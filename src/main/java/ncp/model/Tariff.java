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
public class Tariff {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String title;

    private String description;

    @Column(nullable = false)
    private Long price;

    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private TariffStatus status;

    @ManyToMany
    @JoinTable(name = "tariff_connected_transmitters",
            joinColumns = @JoinColumn(name = "tariff_id"),
            inverseJoinColumns = @JoinColumn(name = "connected_transmitters_id"))
    private Set<Transmitter> connectedTransmitters = new HashSet<>();

    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private User provider;

    @OneToMany(mappedBy = "tariff", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Contract> contracts = new HashSet<>();

}
