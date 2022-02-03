package ncp.repository;

import ncp.model.TariffStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TariffStatusRepository extends JpaRepository<TariffStatus, Long> {


}
