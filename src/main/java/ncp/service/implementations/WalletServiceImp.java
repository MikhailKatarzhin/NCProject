package ncp.service.implementations;

import ncp.model.Wallet;
import ncp.repository.WalletRepository;
import ncp.service.interfaces.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WalletServiceImp implements WalletService {

    private final WalletRepository walletRepository;

    @Autowired
    public WalletServiceImp(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    @Override
    public boolean replenishmentFunds(Long incomingFunds, Long id) {
        try {
            Wallet wallet = walletRepository.findById(id).orElseThrow();
            wallet.replenishmentFunds(incomingFunds);
            walletRepository.save(wallet);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public boolean debitingFunds(Long requiredFunds, Long id) {
        try {
            Wallet wallet = walletRepository.findById(id).orElseThrow();
            if (!wallet.debitingFunds(requiredFunds)) {
                return false;
            }
            walletRepository.save(wallet);
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    @Override
    public Long getBalance(Long id) {
        return walletRepository.findById(id).orElseThrow().getBalance();
    }
}
