package ncp.service.interfaces;

public interface WalletService {

    boolean replenishmentFunds(Long incomingFunds, Long id);

    boolean debitingFunds(Long requiredFunds, Long id);

    Long getBalance(Long id);
}
