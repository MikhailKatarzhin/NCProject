package ncp.service.implementations;

import ncp.model.Contract;
import ncp.model.Wallet;
import ncp.repository.ContractRepository;
import ncp.repository.WalletRepository;
import ncp.service.interfaces.ContractPaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ContractPaymentServiceImp implements ContractPaymentService {

    private final ContractRepository contractRepository;
    private final WalletRepository walletRepository;

    @Autowired
    public ContractPaymentServiceImp(ContractRepository contractRepository, WalletRepository walletRepository) {
        this.contractRepository = contractRepository;
        this.walletRepository = walletRepository;
    }

    @Override
    @Transactional
    public boolean contractPayment(Contract contract) {
        Wallet consumerWallet = contract.getConsumer().getWallet();
        if (consumerWallet.debitingFunds(contract.getTariff().getPrice())) {
            walletRepository.save(consumerWallet);
            contractRepository.addToExpirationDate30DaysById(contract.getId());
            Wallet providerWallet = contract.getTariff().getProvider().getWallet();
            providerWallet.replenishmentFunds(contract.getTariff().getPrice());
            walletRepository.save(providerWallet);
            return true;
        }
        return false;
    }
}
