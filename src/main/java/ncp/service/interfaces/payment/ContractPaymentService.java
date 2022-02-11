package ncp.service.interfaces.payment;

import ncp.model.Contract;

public interface ContractPaymentService {

    boolean contractPayment(Contract contract);
}
