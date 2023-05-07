package ncp.service.implementations.payment;

import ncp.model.Contract;
import ncp.model.Wallet;
import ncp.repository.ContractRepository;
import ncp.repository.WalletRepository;
import ncp.service.interfaces.payment.ContractPaymentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ContractPaymentServiceImp implements ContractPaymentService {

    private final static Logger logger = LoggerFactory.getLogger(ContractPaymentServiceImp.class);

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
        Wallet consumerWallet = walletRepository.getById(contract.getConsumer().getId());
        if (consumerWallet.debitingFunds(contract.getTariff().getPrice())) {
            walletRepository.save(consumerWallet);
            contractRepository.addToExpirationDate30DaysById(contract.getId());
            Wallet providerWallet = walletRepository.getById(contract.getTariff().getProvider().getId());
            providerWallet.replenishmentFunds(contract.getTariff().getPrice());
            walletRepository.save(providerWallet);
            logger.info("Contract [id:{}] paid by {}", contract.getId(), contract.getTariff().getPrice());
            return true;
        }
        logger.warn("Contract [id:{}] payment by {} is failed", contract.getId(), contract.getTariff().getPrice());
        return false;
    }
}
