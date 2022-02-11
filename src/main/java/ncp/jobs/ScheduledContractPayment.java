package ncp.jobs;

import ncp.model.Contract;
import ncp.model.User;
import ncp.model.Wallet;
import ncp.repository.ContractRepository;
import ncp.repository.WalletRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScheduledContractPayment {

    private final static Logger logger = LoggerFactory.getLogger(ScheduledContractPayment.class);

    private final Long rowInPage = 100L;
    private final ContractRepository contractRepository;
    private final WalletRepository walletRepository;

    @Autowired
    public ScheduledContractPayment(ContractRepository contractRepository, WalletRepository walletRepository) {
        this.contractRepository = contractRepository;
        this.walletRepository = walletRepository;
    }

    @Scheduled(cron = "0 0 12 * * *")
    public void contractsPayment() {
        logger.trace("Scheduled payment of contracts has started");

        Long nRow = contractRepository.countExpiredWithNonInactiveTariff();
        logger.debug("Expired contracts quantity: {}", nRow);

        Long confirmedPayment = 0L;
        Long nPage = nRow / rowInPage + (nRow % rowInPage == 0L ? 0L : 1L);
        for (Long i = nPage; i > 0; i--) {
            List<Contract> contractList = contractRepository.selectExpiredWithNonInactiveTariffByLimitOffset(
                    rowInPage, rowInPage * (i - 1L));
            for (Contract contract : contractList) {
                User user = contract.getConsumer();
                Wallet consumerWallet = user.getWallet();
                if (consumerWallet.debitingFunds(contract.getTariff().getPrice())) {
                    walletRepository.save(consumerWallet);
                    contractRepository.addToExpirationDate30DaysById(contract.getId());
                    Wallet providerWallet = contract.getTariff().getProvider().getWallet();
                    providerWallet.replenishmentFunds(contract.getTariff().getPrice());
                    walletRepository.save(providerWallet);
                    confirmedPayment++;
                }
            }
        }
        logger.info("Scheduled payment of contracts has ended. Confirmed payments: {}. Failed payments: {}"
                , confirmedPayment, nRow - confirmedPayment);
    }
}