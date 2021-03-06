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
    private Set<Transmitter> connectedTransmitters = new HashSet<>();

    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private User provider;

    @OneToMany(mappedBy = "tariff", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Contract> contracts = new HashSet<>();

}
