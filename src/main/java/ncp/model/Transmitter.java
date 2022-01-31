package ncp.model;

import lombok.*;

import javax.persistence.*;
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
    private Set<Address> availableAddresses = new java.util.LinkedHashSet<>();

    @ManyToOne
    private Address address;
}
