package ncp.jobs;

import ncp.model.*;
import ncp.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScheduledTariffPayment {

    private final Long rowInPage = 100L;

    private final TariffStatusRepository tariffStatusRepository;
    private final TransmitterRepository transmitterRepository;
    private final AddressRepository addressRepository;
    private final TariffRepository tariffRepository;
    private final WalletRepository walletRepository;

    @Autowired
    public ScheduledTariffPayment(TariffStatusRepository tariffStatusRepository
            , TransmitterRepository transmitterRepository, AddressRepository addressRepository
            , TariffRepository tariffRepository, WalletRepository walletRepository) {
        this.tariffStatusRepository = tariffStatusRepository;
        this.transmitterRepository = transmitterRepository;
        this.addressRepository = addressRepository;
        this.tariffRepository = tariffRepository;
        this.walletRepository = walletRepository;
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void contractsPayment() {
        Long nRow = tariffRepository.countNonInactive();
        Long nPage = nRow / rowInPage + (nRow % rowInPage == 0L ? 0L : 1L);
        for (Long i = nPage; i > 0; i--) {
            List<Tariff> tariffList = tariffRepository.selectNonInactiveByLimitOffset(
                    rowInPage, rowInPage * (i - 1L));
            for (Tariff tariff : tariffList) {
                Long nTransmitterRow = transmitterRepository.countConnectedTransmitterByTariffId(tariff.getId());
                Long nTransmitterPage = nTransmitterRow / rowInPage + (nTransmitterRow % rowInPage == 0L ? 0L : 1L);
                Long price = 0L;
                for (Long y = nTransmitterPage; y > 0; y--) {
                    List<Transmitter> transmitterList = transmitterRepository
                            .selectConnectedTransmitterByLimitOffsetAndId(
                            tariff.getId(), rowInPage, rowInPage * (i - 1L));
                    for (Transmitter transmitter : transmitterList) {
                        price += addressRepository.countConnectedAddressByTransmitterId(transmitter.getId());
                    }
                }
                User user = tariff.getProvider();
                Wallet wallet = user.getWallet();
                if (wallet.debitingFunds(price)) {
                    walletRepository.save(wallet);
                } else {
                    tariff.setStatus(tariffStatusRepository.getByName("inactive"));
                    tariffRepository.save(tariff);
                }
            }
        }
    }
}
