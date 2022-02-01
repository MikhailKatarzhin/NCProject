package ncp.repository;

import ncp.model.Transmitter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransmitterRepository extends JpaRepository<Transmitter, Long> {
}
