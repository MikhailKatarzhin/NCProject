package ncp.service;

import ncp.model.Contract;
import ncp.repository.ContractRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static ncp.config.ProjectConstants.ROW_COUNT;

@Service
public class ContractService {
    @Autowired
    private ContractRepository contractRepository;

///********************! Contract !********************

    public long pageCount (){
        long nTariff = contractRepository.count();
        long nPage = nTariff / ROW_COUNT + (nTariff % ROW_COUNT == 0 ? 0 : 1);
        return nPage == 0 ? nPage + 1 : nPage;
    }

    public List<Contract> contractListByTariffIdAndNumberPageList(Long tariffId, Long numberPageList){
        return contractRepository.selectByTariffIdLimitOffset(
                tariffId, ROW_COUNT, (numberPageList-1)*ROW_COUNT);
    }

    public Long countContractByTariffId(Long tariffId){
        return contractRepository.countByTariffId(tariffId);
    }

    public List<Contract> contractListByConsumerIdAndNumberPageList(Long consumerId, Long numberPageList){
        return contractRepository.selectByTariffIdLimitOffset(
                consumerId, ROW_COUNT, (numberPageList-1)*ROW_COUNT);
    }

    public Long countContractByConsumerId(Long consumerId){
        return contractRepository.countByConsumerId(consumerId);
    }

    public void terminateContractById(Long contractId){
        contractRepository.deleteById(contractId);
    }
}
