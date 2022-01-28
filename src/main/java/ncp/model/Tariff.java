package ncp.model;

import lombok.*;

import javax.persistence.*;
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

    @ManyToMany(cascade = CascadeType.ALL)
    private Set<Transmitter> connectedTransmitters = new java.util.LinkedHashSet<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn
    private Set<Contract> signedContracts = new java.util.LinkedHashSet<>();
}
