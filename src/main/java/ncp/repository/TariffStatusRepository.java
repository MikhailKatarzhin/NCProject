package ncp.repository;

import ncp.model.TariffStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TariffStatusRepository extends JpaRepository<TariffStatus, Long> {
    @Query(
            value = "SELECT * FROM tariff_status WHERE name =?"
            , nativeQuery = true
    )
    TariffStatus getByName(String name);

}
