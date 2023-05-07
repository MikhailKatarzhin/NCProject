package ncp.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@RequiredArgsConstructor
@Table
@Entity
public class Role implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, updatable = false)
    private String name;

    @ManyToMany
    @JoinTable(name = "user_role_set",
            joinColumns = @JoinColumn(name = "role_set_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> users = new LinkedHashSet<>();

    @Override
    public String getAuthority() {
        return getName();
    }
}
