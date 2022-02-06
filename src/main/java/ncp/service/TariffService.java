package ncp.service;

import ncp.model.Address;
import ncp.model.Contract;
import ncp.model.Tariff;
import ncp.model.Transmitter;
import ncp.repository.TariffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import java.util.HashSet;
import java.util.List;

import static ncp.config.ProjectConstants.ROW_COUNT;

@Service
public class TariffService {

    @Autowired
    private TariffRepository tariffRepository;
    @Autowired
    private TariffStatusService tariffStatusService;
    @Autowired
    private TransmitterService transmitterService;
    @Autowired
    private UserService userService;
    @Autowired
    private  ContractService contractService;

///********************! Tariff !********************

    public long pageCount (){
        long nTariff = tariffRepository.count();
        long nPage = nTariff / ROW_COUNT + (nTariff % ROW_COUNT == 0 ? 0 : 1);
        return nPage == 0 ? nPage + 1 : nPage;
    }

    public List<Tariff> tariffListByNumberPageList (long numberPageList){
        return tariffRepository.selectByLimitOffset(ROW_COUNT, (numberPageList-1)*ROW_COUNT);
    }

    public List<Tariff> ownTariffListByNumberPageList (long numberPageList, Long userId){
        return tariffRepository.selectByLimitOffsetAndProviderId(
                ROW_COUNT, (numberPageList-1)*ROW_COUNT, userId);
    }

    public ModelMap validateNewTariff(Tariff tariff, ModelMap model){
        if (tariffRepository.getByTitle(tariff.getTitle()) != null){
            model.addAttribute("titleError", "Title already exist");
            model.addAttribute("hasError", true);
        }else if(tariff.getTitle().isBlank()){
            model.addAttribute("titleError", "Title is blank");
            model.addAttribute("hasError", true);
        }
        if (tariff.getPrice() < 0) {
            model.addAttribute("priceError", "Price negative");
            model.addAttribute("hasError", true);
        }
        return model;
    }

    public Tariff saveNew(Tariff tariff){
        if (tariff == null)
            return null;
        tariff.setStatus(tariffStatusService.getById(1));
        tariff.setConnectedTransmitters(new HashSet<>());
        tariff.setProvider(userService.getRemoteUser());
        tariff = tariffRepository.save(tariff);
        return tariff;
    }

    public void deleteById(long id){
        tariffRepository.deleteById(id);
    }

    public Tariff getById(long id){
        return tariffRepository.getById(id);
    }

    ///********************! Tariff sets !********************

    public Tariff setDescription (long tariffId, String description){
        Tariff tariff = tariffRepository.getById(tariffId);
        tariff.setDescription(description);
        return tariffRepository.save(tariff);
    }

    public Tariff setPrice (long tariffId, long price){
        Tariff tariff = tariffRepository.getById(tariffId);
        tariff.setPrice(price);
        return tariffRepository.save(tariff);
    }

    public Tariff setStatus (long tariffId, long statusId){
        Tariff tariff = tariffRepository.getById(tariffId);
        tariff.setStatus(tariffStatusService.getById(statusId));
        return tariffRepository.save(tariff);
    }

///********************! Connected Transmitter !********************

    public long connectedTransmitterPageCount (long transmitterId){
        long nConnectedTransmitter = tariffRepository.countConnectedTransmitterById(transmitterId);
        long nPage = nConnectedTransmitter / ROW_COUNT + (nConnectedTransmitter % ROW_COUNT == 0 ? 0 : 1);
        return nPage == 0? nPage + 1 : nPage;
    }

    public List<Transmitter> connectedTransmitterListByNumberPageListAndTariffId (long numberPageList, Long tariffId){
        return transmitterService.connectedTransmitterListByNumberPageListAndTransmitterId (numberPageList, tariffId);
    }

    public void  addConnectedTransmitter(Long tariffId, Long transmitterId){
        tariffRepository.InsertTariffConnectedTransmitters(tariffId, transmitterId);
    }

    public void removeConnectedTransmitterByTransmitterIdAndTariffId(Long transmitterId, Long tariffId){
        tariffRepository.removeConnectedTransmitterByTransmitterIdAndTariffId(transmitterId, tariffId);
    }

///********************! Search connectable Transmitters !********************

    public List<Transmitter> searchTransmitterByTransmitterAvailableAddressIdWithoutConnectedTransmitterId(
            Address address, Long numberPage, Long tariffId){
        return transmitterService.searchTransmitterByTransmitterAvailableAddressIdWithoutConnectedTransmitterId(
                address, numberPage, tariffId);
    }

///********************! Signed Contracts !********************

    public List<Contract> signedContractListsByTariffId(Long tariffId, Long numberPageList){
        return contractService.contractListByTariffIdAndNumberPageList(
                tariffId, numberPageList);
    }

    public Long countContractByTariffId(Long tariffId){
        long nConnectedTransmitter = contractService.countContractByTariffId(tariffId);
        long nPage = nConnectedTransmitter / ROW_COUNT + (nConnectedTransmitter % ROW_COUNT == 0 ? 0 : 1);
        return nPage == 0? nPage + 1 : nPage;
    }

    public void terminateContractById(Long contractId){
        contractService.terminateContractById(contractId);
    }
}
