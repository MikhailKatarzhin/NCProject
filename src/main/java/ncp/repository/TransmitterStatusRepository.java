package ncp.repository;

import ncp.model.TransmitterStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TransmitterStatusRepository extends JpaRepository<TransmitterStatus, Long> {
    @Query(
            value = "SELECT * FROM transmitter_status WHERE name =?"
            , nativeQuery = true
    )
    TransmitterStatus getByName(String statusName);
}
