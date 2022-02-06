package ncp.service.interfaces;

import ncp.model.Address;
import ncp.model.Contract;
import ncp.model.Tariff;
import ncp.model.User;

import java.util.List;

public interface ContractService {

    Long pageCount(Long consumerId);

    List<Contract> contractListByTariffIdAndNumberPageList(Long tariffId, Long numberPageList);

    Long countContractByTariffId(Long tariffId);

    List<Contract> contractListByConsumerIdAndNumberPageList(Long consumerId, Long numberPageList);

    Long countContractByConsumerId(Long consumerId);

    void terminateContractById(Long contractId);

    Contract signContract(Tariff tariff, User consumer, Address address);
}
