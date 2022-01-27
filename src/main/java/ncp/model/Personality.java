package ncp.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Table
@Entity
@ToString
@RequiredArgsConstructor
@Getter
public class Personality {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long        id;

    private String      first_name;

    private String      last_name;

    private String      patronymic;
}
