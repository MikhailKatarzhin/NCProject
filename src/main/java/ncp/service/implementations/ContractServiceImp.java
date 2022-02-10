package ncp.service.implementations;

import ncp.model.Address;
import ncp.model.Contract;
import ncp.model.Tariff;
import ncp.model.User;
import ncp.repository.ContractRepository;
import ncp.service.interfaces.ContractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;

import static ncp.config.ProjectConstants.ROW_COUNT;

@Service
public class ContractServiceImp implements ContractService {

    private final ContractRepository contractRepository;

    @Autowired
    public ContractServiceImp(ContractRepository contractRepository) {
        this.contractRepository = contractRepository;
    }

///********************! Contract !********************

    public Long pageCount(Long consumerId) {
        Long nRow = countContractByConsumerId(consumerId);
        Long nPage = nRow / ROW_COUNT + (nRow % ROW_COUNT == 0 ? 0 : 1);
        return nPage == 0 ? nPage + 1 : nPage;
    }

    public List<Contract> contractListByTariffIdAndNumberPageList(Long tariffId, Long numberPageList) {
        return contractRepository.selectByTariffIdLimitOffset(
                tariffId, ROW_COUNT, (numberPageList - 1) * ROW_COUNT);
    }

    public Long countContractByTariffId(Long tariffId) {
        return contractRepository.countByTariffId(tariffId);
    }

    public List<Contract> contractListByConsumerIdAndNumberPageList(Long consumerId, Long numberPageList) {
        return contractRepository.selectByConsumerIdLimitOffset(
                consumerId, ROW_COUNT, (numberPageList - 1) * ROW_COUNT);
    }

    public Long countContractByConsumerId(Long consumerId) {
        return contractRepository.countByConsumerId(consumerId);
    }

    public void terminateContractById(Long contractId) {
        contractRepository.deleteById(contractId);
    }

    @Override
    public Contract signContract(Tariff tariff, User consumer, Address address) {
        Contract contract = new Contract();
        contract.setTariff(tariff);
        contract.setAddress(address);
        contract.setConsumer(consumer);
        contract.setTitle(tariff.getTitle());
        contract.setContractExpirationDate(new Date(System.currentTimeMillis() + 2_592_000_000L));
        contract.setDescription("Signed: " + contract.getContractExpirationDate().toString()
                + ". Tariff description: " + tariff.getDescription() + ".");
        return contractRepository.save(contract);
    }

    @Override
    public List<Contract> listExpiredContractWithNonInactiveTariffByLimitOffsetByConsumerIdLimitOffset(
            Long consumerId, Long limit, Long offset) {
        return contractRepository.selectExpiredWithNonInactiveTariffByLimitOffsetByConsumerIdLimitOffset(
                consumerId, limit, offset);
    }

    @Override
    public Long countExpiredContractWithNonInactiveTariffByLimitOffsetByConsumerId(Long consumerId) {
        return contractRepository.countExpiredWithNonInactiveTariffByConsumerId(consumerId);
    }

    @Override
    public void addToExpirationDate30DaysSinceTodayById(Long id) {
        contractRepository.addToExpirationDate30DaysById(id);
    }
}
