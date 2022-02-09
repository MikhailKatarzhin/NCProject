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

    private String firstName = "";

    private String lastName = "";

    private String patronymic = "";
}
