package ncp.repository;

import ncp.model.Contract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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
            value = "SELECT * FROM contract" +
                    " INNER JOIN tariff t on contract.tariff_id = t.id" +
                    " INNER JOIN tariff_status ts on t.status_id = ts.id" +
                    " WHERE ts.name <> 'inactive'" +
                    " AND contract_expiration_date <= current_date" +
                    " AND consumer_id = ?1" +
                    " ORDER BY price" +
                    " LIMIT ?2 OFFSET ?3"
            , nativeQuery = true
    )
    List<Contract> selectExpiredWithNonInactiveTariffByLimitOffsetByConsumerIdLimitOffset(
            Long consumerId, Long limit, Long offset);

    @Query(
            value = "SELECT COUNT(*) FROM contract" +
                    " INNER JOIN tariff t on contract.tariff_id = t.id" +
                    " INNER JOIN tariff_status ts on t.status_id = ts.id" +
                    " WHERE ts.name <> 'inactive'" +
                    " AND contract_expiration_date <= current_date" +
                    " AND consumer_id = ?1"
            , nativeQuery = true
    )
    Long countExpiredWithNonInactiveTariffByConsumerId(Long consumerId);

    @Query(
            value = "SELECT * FROM contract" +
                    " INNER JOIN tariff t on contract.tariff_id = t.id" +
                    " INNER JOIN tariff_status ts on t.status_id = ts.id" +
                    " WHERE ts.name <> 'inactive'" +
                    " AND contract_expiration_date <= current_date" +
                    " LIMIT ?1 OFFSET ?2"
            , nativeQuery = true
    )
    List<Contract> selectExpiredWithNonInactiveTariffByLimitOffset(Long limit, Long offset);

    @Query(
            value = "SELECT COUNT(*) FROM contract" +
                    " INNER JOIN tariff t on contract.tariff_id = t.id" +
                    " INNER JOIN tariff_status ts on t.status_id = ts.id" +
                    " WHERE ts.name <> 'inactive'" +
                    " AND contract_expiration_date <= current_date"
            , nativeQuery = true
    )
    Long countExpiredWithNonInactiveTariff();

    @Transactional
    @Modifying
    @Query(
            value = "UPDATE contract" +
                    " SET contract_expiration_date = CURRENT_DATE + INTERVAL '30 days'" +
                    " WHERE id = ?"
            , nativeQuery = true
    )
    void addToExpirationDate30DaysById(Long id);

    @Query(
            value = "SELECT COUNT(*) FROM contract" +
                    " WHERE tariff_id = ?"
            , nativeQuery = true
    )
    Long countByTariffId(Long tariffId);

    @Query(
            value = "SELECT COUNT(*) FROM contract" +
                    " WHERE consumer_id = ?"
            , nativeQuery = true
    )
    Long countByConsumerId(Long tariffId);
}
