package ncp.repository;

import ncp.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    @Query(
            value = "select * from role r where r.name <> ?",
            nativeQuery = true
    )
    List<Role> findAllExceptName(String exceptingName);
}
