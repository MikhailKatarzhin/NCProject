package ncp.model;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@RequiredArgsConstructor
@Table
@Entity
public class Contract {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;

    @Column(nullable = false, unique = true)
    private String title;

    @ManyToOne(optional = false, targetEntity = Address.class)
    private Address address;
}
