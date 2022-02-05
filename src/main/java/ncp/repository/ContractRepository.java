package ncp.repository;

import ncp.model.Contract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContractRepository extends JpaRepository<Contract, Long> {

    @Query(
            value = "SELECT * FROM contract" +
                    " WHERE tariff_id = ?1" +
                    " LIMIT ?2 OFFSET ?3"
            , nativeQuery = true
    )
    List<Contract> selectByTariffIdLimitOffset(Long tariffId, Long limit, Long offset);

    @Query(
            value = "SELECT * FROM contract" +
                    " WHERE consumer_id  = ?1" +
                    " LIMIT ?2 OFFSET ?3"
            , nativeQuery = true
    )
    List<Contract> selectByConsumerIdLimitOffset(Long consumerId, Long limit, Long offset);

    @Query(
            value = "SELECT COUNT(*) FROM contract" +
                    " WHERE tariff_id = ?"
            , nativeQuery = true
    )
    Long countSignedContractByTariffId(Long tariffId);
}
