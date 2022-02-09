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
            value = "SELECT * FROM tariff" +
                    " INNER JOIN tariff_status ts on tariff.status_id = ts.id" +
                    " WHERE ts.name <> 'inactive'" +
                    " LIMIT ?1 OFFSET ?2"
            , nativeQuery = true
    )
    List<Tariff> selectNonInactiveByLimitOffset(Long limit, Long offset);

    @Query(
            value = "SELECT COUNT(*) FROM tariff" +
                    " INNER JOIN tariff_status ts on tariff.status_id = ts.id" +
                    " WHERE ts.name <> 'inactive'"
            , nativeQuery = true
    )
    Long countNonInactive();

    @Query(
            value = "SELECT * FROM tariff t" +
                    " WHERE t.provider_id = ?3" +
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
            value = "INSERT INTO tariff_connected_transmitters(tariff_id, connected_transmitters_id) VALUES (?1, ?2)"
            , nativeQuery = true
    )
    void InsertTariffConnectedTransmitters(Long tariffId, Long transmitterId);

    @Query(
            value = "SELECT * FROM tariff WHERE title = ?"
            , nativeQuery = true
    )
    Tariff getByTitle(String title);

    @Query(
            value = "SELECT t.* FROM tariff t " +
                    "INNER JOIN tariff_connected_transmitters tct " +
                    "ON t.id = tct.tariff_id " +
                    "INNER JOIN transmitter t2 " +
                    "ON t2.id = tct.connected_transmitters_id " +
                    "INNER JOIN transmitter_status ts " +
                    "ON t2.status_id = ts.id " +
                    "INNER JOIN transmitter_available_addresses taa " +
                    "ON tct.connected_transmitters_id = taa.transmitter_id " +
                    "INNER JOIN address a " +
                    "ON taa.available_addresses_id = a.id " +
                    "INNER JOIN tariff_status s " +
                    "ON t.status_id = s.id " +
                    "WHERE a.id = ?3 AND ts.name = 'turned on' AND s.name = 'active' " +
                    "ORDER BY t.provider_id " +
                    "LIMIT ?1 OFFSET ?2"
            , nativeQuery = true
    )
    List<Tariff> selectByLimitOffsetAndAvailableAddressIdAndTurnedOnTransmitterAndActiveTariff(
            Long limit, Long offset, Long availableAddressId);

    @Query(
            value = "SELECT COUNT(*) FROM tariff t " +
                    "INNER JOIN tariff_connected_transmitters tct " +
                    "ON t.id = tct.tariff_id " +
                    "INNER JOIN transmitter t2 " +
                    "ON t2.id = tct.connected_transmitters_id " +
                    "INNER JOIN transmitter_status ts " +
                    "ON t2.status_id = ts.id " +
                    "INNER JOIN transmitter_available_addresses taa " +
                    "ON tct.connected_transmitters_id = taa.transmitter_id " +
                    "INNER JOIN address a " +
                    "ON taa.available_addresses_id = a.id " +
                    "INNER JOIN tariff_status s " +
                    "ON t.status_id = s.id " +
                    "WHERE a.id = ?1 AND ts.name = ?2 AND s.name = ?3 "
            , nativeQuery = true
    )
    Long countByAvailableAddressIdAndTransmitterStatusAndTariffStatus(
            Long availableAddressId, String transmitterStatus, String tariffStatus);

    @Query(
            value = "SELECT COUNT(*) FROM tariff_connected_transmitters tct" +
                    " INNER JOIN transmitter t" +
                    " ON tct.connected_transmitters_id = t.id" +
                    " INNER JOIN transmitter_available_addresses taa" +
                    " ON t.id = taa.transmitter_id" +
                    " WHERE tariff_id = ?"
            , nativeQuery = true
    )
    Long countConnectedAddressByTariffId(Long tariffId);
}
