package ncp.service.implementations;

import ncp.model.Tariff;
import ncp.model.Wallet;
import ncp.repository.TariffRepository;
import ncp.repository.TariffStatusRepository;
import ncp.repository.WalletRepository;
import ncp.service.interfaces.TariffPaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TariffPaymentServiceImp implements TariffPaymentService {

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
            return true;
        } else {
            tariff.setStatus(tariffStatusRepository.getByName("inactive"));
            tariffRepository.save(tariff);
            return false;
        }
    }
}
