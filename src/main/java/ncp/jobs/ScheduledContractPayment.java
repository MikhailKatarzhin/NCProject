package ncp.jobs;

import ncp.model.Contract;
import ncp.model.User;
import ncp.model.Wallet;
import ncp.repository.ContractRepository;
import ncp.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScheduledContractPayment {

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
        Long nRow = contractRepository.countExpiredWithNonInactiveTariffByLimitOffset();
        Long nPage = nRow / rowInPage + (nRow % rowInPage == 0L ? 0L : 1L);
        for (Long i = nPage; i > 0; i--) {
            List<Contract> contractList = contractRepository.selectExpiredWithNonInactiveTariffByLimitOffset(rowInPage, rowInPage * (i-1L));
            for (Contract contract : contractList) {
                User user = contract.getConsumer();
                Wallet wallet = user.getWallet();
                if (wallet.debitingFunds(contract.getTariff().getPrice())) {
                    walletRepository.save(wallet);
                    contractRepository.addToExpirationDate30DaysById(contract.getId());
                }
            }
        }
    }
}
