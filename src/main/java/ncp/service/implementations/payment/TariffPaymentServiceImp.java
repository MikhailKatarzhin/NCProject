package ncp.service.implementations.payment;

import ncp.model.Tariff;
import ncp.model.Wallet;
import ncp.repository.TariffRepository;
import ncp.repository.TariffStatusRepository;
import ncp.repository.WalletRepository;
import ncp.service.interfaces.payment.TariffPaymentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TariffPaymentServiceImp implements TariffPaymentService {

    private final static Logger logger = LoggerFactory.getLogger(TariffPaymentServiceImp.class);

    private final TariffRepository tariffRepository;
    private final TariffStatusRepository tariffStatusRepository;
    private final WalletRepository walletRepository;

    @Autowired
    public TariffPaymentServiceImp(TariffRepository tariffRepository, TariffStatusRepository tariffStatusRepository
            , WalletRepository walletRepository) {
        this.tariffStatusRepository = tariffStatusRepository;
        this.tariffRepository = tariffRepository;
        this.walletRepository = walletRepository;
    }

    @Override
    @Transactional
    public boolean tariffPayment(Tariff tariff) {
        Long price = tariffRepository.countConnectedAddressByTariffId(tariff.getId());
        Wallet wallet = tariff.getProvider().getWallet();
        if (wallet.debitingFunds(price)) {
            walletRepository.save(wallet);
            logger.info("Tariff [id:{}] paid by {}", tariff.getId(), price);
            return true;
        } else {
            tariff.setStatus(tariffStatusRepository.getByName("inactive"));
            tariffRepository.save(tariff);
            logger.info("Provider of tariff [id:{}] have not enough founds for paid by {}. This tariff inactivated."
                    , tariff.getId(), price);
            return false;
        }
    }
}
