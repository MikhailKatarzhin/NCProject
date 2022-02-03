package ncp.repository;

import ncp.model.Transmitter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransmitterRepository extends JpaRepository<Transmitter, Long> {
    @Query(
            value = "SELECT * FROM transmitter ORDER BY id LIMIT ?1 OFFSET ?2"
            , nativeQuery = true
    )
    List<Transmitter> selectByLimitOffset(Long limit, Long offset);
}
