package ncp.model;

import lombok.*;

import javax.persistence.*;

@Table
@Entity
@ToString
@Getter
@Setter
@RequiredArgsConstructor
public class Personality {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name")
    private String firstName = "";

    @Column(name = "last_name")
    private String lastName = "";

    private String patronymic = "";

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id", nullable = false)
    private User user;
}
