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

    @Column(updatable = false)
    private String description;

    @Column(nullable = false, updatable = false)
    private String title;

    @ManyToOne(optional = false, targetEntity = Address.class)
    @JoinColumn(nullable = false, updatable = false)
    private Address address;

    @Column(nullable = false)
    private Date contractExpirationDate;

    @ManyToOne(optional = false)
    @JoinColumn(nullable = false, updatable = false)
    private Tariff tariff;

    @ManyToOne(optional = false)
    @JoinColumn(nullable = false, updatable = false)
    private User consumer;

}
