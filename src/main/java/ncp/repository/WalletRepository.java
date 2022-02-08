package ncp.repository;

import ncp.model.Wallet;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WalletRepository  extends CrudRepository<Wallet, Long> {
}
