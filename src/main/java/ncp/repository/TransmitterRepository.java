package ncp.repository;

import ncp.model.Address;
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
            value = "SELECT a.* FROM transmitter_available_addresses taa" +
                    " INNER JOIN transmitter t ON taa.transmitter_id = t.id" +
                    " INNER JOIN address a ON taa.available_addresses_id = a.id" +
                    " WHERE t.id=?3" +
                    " LIMIT ?1 OFFSET ?2"
            , nativeQuery = true
    )
    List<Address> selectAvailableAddressByLimitOffsetAndId(Long limit, Long offset, Long transmitterId);

    @Query(
            value = "SELECT COUNT(*) FROM transmitter t" +
                    " INNER JOIN transmitter_available_addresses taa on t.id = taa.transmitter_id" +
                    " INNER JOIN address a on t.address_id = a.id" +
                    " WHERE t.id=?"
            , nativeQuery = true
    )
    Long countAvailableAddressById(Long id);

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
}
