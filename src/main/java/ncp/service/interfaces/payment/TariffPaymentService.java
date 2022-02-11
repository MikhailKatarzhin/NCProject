package ncp.service.interfaces.payment;

import ncp.model.Tariff;

public interface TariffPaymentService {

    boolean tariffPayment(Tariff tariff);
}
