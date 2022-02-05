package ncp.model;

import lombok.*;

import javax.persistence.*;
import java.sql.Date;

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

    @Column(nullable = false)
    private String title;

    @ManyToOne(optional = false, targetEntity = Address.class)
    private Address address;

    @Column(nullable = false)
    private Date contractExpirationDate;

    @ManyToOne(cascade = CascadeType.ALL, optional = false)
    @JoinColumn(nullable = false)
    private Tariff tariff;

    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private User consumer;

}
