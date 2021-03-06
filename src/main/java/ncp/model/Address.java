package ncp.model;

import lombok.*;

import javax.persistence.*;

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

    @Override
    public String toString() {
        return country +
                (region.isBlank() ? "" : (", rg " + region)) +
                ", " + city +
                (street.isBlank() ? "" : (", st " + street)) +
                ", hse " + house +
                "/" + building +
                (flat == null ? "" : (", ste " + flat))
                ;
    }
}
