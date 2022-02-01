package ncp.repository;

import ncp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);

    @Query(
            value = "select count(*) from user where email = ?",
            nativeQuery = true
    )
    Long countByEmail(String email);
}
