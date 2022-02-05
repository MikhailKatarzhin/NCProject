package ncp.repository;

import ncp.model.Tariff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface TariffRepository extends JpaRepository<Tariff, Long> {
    @Query(
            value = "SELECT * FROM tariff ORDER BY id LIMIT ?1 OFFSET ?2"
            , nativeQuery = true
    )
    List<Tariff> selectByLimitOffset(Long limit, Long offset);

    @Query(
            value = "SELECT * FROM tariff t" +
                    " INNER JOIN user u on t.provider_id = u.id" +
                    " ORDER BY t.id" +
                    " LIMIT ?1 OFFSET ?2"
            , nativeQuery = true
    )
    List<Tariff> selectByLimitOffsetAndProviderId(Long limit, Long offset, Long providerId);

    @Query(
            value = "SELECT COUNT(*) FROM tariff t" +
                    " INNER JOIN tariff_connected_transmitters tct on t.id = tct.tariff_id" +
                    " INNER JOIN transmitter t2 on tct.connected_transmitters_id = t2.id" +
                    " WHERE t.id=?"
            , nativeQuery = true
    )
    Long countConnectedTransmitterById(Long id);

    @Transactional
    @Modifying
    @Query(
            value = "DELETE FROM tariff_connected_transmitters WHERE connected_transmitters_id=?1 AND tariff_id=?2"
            , nativeQuery = true
    )
    void removeConnectedTransmitterByTransmitterIdAndTariffId(Long transmitterId, Long tariffId);

    @Transactional
    @Modifying
    @Query(
            value = "INSERT INTO tariff_connected_transmitters(tariff_id, connected_transmitters_id) VALUES (?2, ?1)"
            , nativeQuery = true
    )
    void addConnectedTransmitterByTransmitterIdAndTariffId(Long transmitterId, Long tariffId);

    @Query(
            value = "SELECT * FROM tariff WHERE title = ?"
            , nativeQuery = true
    )
    Tariff getByTitle(String title);
}
