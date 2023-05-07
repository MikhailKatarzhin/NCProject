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
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String country;

    /**
     * null - there is no separation into regions
     * As example Vatican
     */
    private String region;

    @Column(nullable = false)
    private String city;

    /**
     * null - there is no separation into streets
     */
    private String street;

    @Column(nullable = false)
    private Long house;

    @Column(nullable = false)
    private Long building;

    /**
     * null - there is no separation into flats
     */
    private Long flat;

    @ManyToMany
    @JoinTable(name = "transmitter_available_addresses",
            joinColumns = @JoinColumn(name = "available_addresses_id"),
            inverseJoinColumns = @JoinColumn(name = "transmitter_id"))
    private Set<Transmitter> transmitters = new HashSet<>();

    @Override
    public String toString() {
        return country +
                (region.isBlank() || region == null ? "" : (", rg " + region)) +
                (city.isBlank() || city == null ? "" : (", " + city)) +
                (street.isBlank() || street == null ? "" : (", st " + street)) +
                (house == null ? "" : (", hse " + house)) +
                (building == null ? "" : ("/" + building)) +
                (flat == null ? "" : (", ste " + flat))
                ;
    }
}
