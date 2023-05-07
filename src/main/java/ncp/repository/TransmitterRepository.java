package ncp.repository;

import ncp.model.Transmitter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface TransmitterRepository extends JpaRepository<Transmitter, Long> {
    @Query(
            value = "SELECT * FROM transmitter ORDER BY id LIMIT ?1 OFFSET ?2"
            , nativeQuery = true
    )
    List<Transmitter> selectByLimitOffset(Long limit, Long offset);

    @Query(
            value = "SELECT COUNT(*) FROM transmitter t" +
                    " INNER JOIN transmitter_available_addresses taa on t.id = taa.transmitter_id" +
                    " WHERE t.id=?"
            , nativeQuery = true
    )
    Long countAvailableAddressByTransmitterId(Long transmitterId);

    @Transactional
    @Modifying
    @Query(
            value = "DELETE FROM transmitter_available_addresses WHERE transmitter_id=?1 AND available_addresses_id=?2"
            , nativeQuery = true
    )
    void removeAvailableAddressByTransmitterIdAndAddressId(Long tId, Long aId);

    @Transactional
    @Modifying
    @Query(
            value = "INSERT INTO transmitter_available_addresses(transmitter_id, available_addresses_id) VALUES (?1, ?2)"
            , nativeQuery = true
    )
    void addAvailableAddressByTransmitterIdAndAddressId(Long tId, Long aId);

    @Query(
            value = "SELECT t.*" +
                    " FROM tariff_connected_transmitters tct" +
                    " INNER JOIN transmitter t ON tct.connected_transmitters_id = t.id" +
                    " INNER JOIN tariff t2 on tct.tariff_id = t2.id" +
                    " WHERE t2.id=?1" +
                    " LIMIT ?2 OFFSET ?3"
            , nativeQuery = true
    )
    List<Transmitter> selectConnectedTransmitterByLimitOffsetAndId(Long tariffId, Long limit, Long offset);

    @Query(
            value = "SELECT COUNT(t.id)" +
                    " FROM tariff_connected_transmitters tct" +
                    " INNER JOIN transmitter t ON tct.connected_transmitters_id = t.id" +
                    " INNER JOIN tariff t2 on tct.tariff_id = t2.id" +
                    " WHERE t2.id = ?"
            , nativeQuery = true
    )
    Long countConnectedTransmitterByTariffId(Long tariffId);

    @Query(
            value = "SELECT DISTINCT t.* FROM transmitter t\n" +
                    "    LEFT JOIN tariff_connected_transmitters tct\n" +
                    "        ON t.id = tct.connected_transmitters_id AND tct.tariff_id = ?10\n" +
                    "    INNER JOIN transmitter_available_addresses taa ON t.id = taa.transmitter_id\n" +
                    "    INNER JOIN address a on a.id = taa.available_addresses_id\n" +
                    "WHERE tct.connected_transmitters_id IS NULL AND tct.tariff_id IS NULL\n" +
                    "  AND a.country LIKE ?1 AND a.region LIKE ?2 AND a.city LIKE ?3 AND a.street LIKE ?4\n" +
                    "  AND CAST (a.house AS TEXT) LIKE ?5 AND CAST (a.building AS TEXT) LIKE ?6 AND CAST (a.flat AS TEXT) LIKE ?7\n" +
                    "LIMIT ?8 OFFSET ?9"
            , nativeQuery = true
    )
    List<Transmitter> searchTransmitterByTransmitterAvailableAddressIdWithoutConnectedTransmitterId(
            String country, String region, String city, String street
            , String house, String building, String flat, Long limit
            , Long offset, Long tariffId);
}
