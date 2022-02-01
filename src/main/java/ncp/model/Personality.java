package ncp.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Table
@Entity
@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Personality {
    @Id
    private Long        id;

    private String      firstName;

    private String      lastName;

    private String      patronymic;
}
