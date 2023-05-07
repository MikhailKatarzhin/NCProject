package ncp.service.interfaces;

import ncp.model.Wallet;

public interface WalletService {

    Wallet getById(long id);

    boolean replenishmentFunds(Long incomingFunds, Long id);

    boolean debitingFunds(Long requiredFunds, Long id);

    Long getBalance(Long id);
}
