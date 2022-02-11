package ncp.jobs;

import ncp.model.*;
import ncp.repository.*;
import ncp.service.interfaces.TariffPaymentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScheduledTariffPayment {

    private final static Logger logger = LoggerFactory.getLogger(ScheduledTariffPayment.class);

    private final Long rowInPage = 100L;

    private final TariffRepository tariffRepository;
    private final TariffPaymentService tariffPaymentService;

    @Autowired
    public ScheduledTariffPayment(TariffRepository tariffRepository, TariffPaymentService tariffPaymentService) {
        this.tariffRepository = tariffRepository;
        this.tariffPaymentService = tariffPaymentService;
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void tariffsPayment() {
        logger.trace("Scheduled payment of tariffs has started");

        Long nRow = tariffRepository.countNonInactive();
        logger.debug("Tariffs quantity: {}", nRow);

        Long inactivatedTariffs = 0L;
        Long nPage = nRow / rowInPage + (nRow % rowInPage == 0L ? 0L : 1L);
        for (Long i = nPage; i > 0; i--) {
            List<Tariff> tariffList = tariffRepository.selectNonInactiveByLimitOffset(
                    rowInPage, rowInPage * (i - 1L));
            for (Tariff tariff : tariffList) {
                if (!tariffPaymentService.tariffPayment(tariff))
                    inactivatedTariffs++;
            }
        }
        logger.info("Scheduled payment of tariffs has ended. Confirmed payments: {}. Inactivated tariffs: {}"
                , nRow - inactivatedTariffs, inactivatedTariffs);
    }
}
