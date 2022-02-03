package ncp.repository;

import ncp.model.TransmitterStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransmitterStatusRepository extends JpaRepository<TransmitterStatus, Long> {
}
