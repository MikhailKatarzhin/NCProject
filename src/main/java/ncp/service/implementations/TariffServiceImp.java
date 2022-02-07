package ncp.service.implementations;

import ncp.model.Address;
import ncp.model.Contract;
import ncp.model.Tariff;
import ncp.model.Transmitter;
import ncp.repository.TariffRepository;
import ncp.service.interfaces.TariffService;
import ncp.service.interfaces.TariffStatusService;
import ncp.service.interfaces.UserService;
import ncp.service.interfaces.ContractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import java.util.HashSet;
import java.util.List;

import static ncp.config.ProjectConstants.ROW_COUNT;

@Service
public class TariffServiceImp implements TariffService {

    private final TariffRepository tariffRepository;
    private final TariffStatusService tariffStatusService;
    private final TransmitterService transmitterService;
    private final UserService userService;
    private final ContractService contractService;

    @Autowired
    TariffServiceImp(TariffRepository tariffRepository, TariffStatusService tariffStatusService
            , TransmitterService transmitterService, UserService userService, ContractService contractService){
        this.tariffStatusService = tariffStatusService;
        this.transmitterService = transmitterService;
        this.tariffRepository = tariffRepository;
        this.contractService = contractService;
        this.userService = userService;
    }

///********************! Tariff !********************

    public Long pageCount (){
        Long nTariff = tariffRepository.count();
        Long nPage = nTariff / ROW_COUNT + (nTariff % ROW_COUNT == 0 ? 0 : 1);
        return nPage == 0 ? nPage + 1 : nPage;
    }

    public List<Tariff> tariffListByNumberPageList (Long numberPageList){
        return tariffRepository.selectByLimitOffset(ROW_COUNT, (numberPageList-1)*ROW_COUNT);
    }

    public List<Tariff> ownTariffListByNumberPageList (Long numberPageList, Long userId){
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
        tariff.setStatus(tariffStatusService.getById(1L));
        tariff.setConnectedTransmitters(new HashSet<>());
        tariff.setProvider(userService.getRemoteUser());
        tariff = tariffRepository.save(tariff);
        return tariff;
    }

    public void deleteById(Long id){
        tariffRepository.deleteById(id);
    }

    public Tariff getById(Long id){
        return tariffRepository.getById(id);
    }

    ///********************! Tariff sets !********************

    public Tariff setDescription (Long tariffId, String description){
        Tariff tariff = tariffRepository.getById(tariffId);
        tariff.setDescription(description);
        return tariffRepository.save(tariff);
    }

    public Tariff setPrice (Long tariffId, Long price){
        Tariff tariff = tariffRepository.getById(tariffId);
        tariff.setPrice(price);
        return tariffRepository.save(tariff);
    }

    public Tariff setStatus (Long tariffId, Long statusId){
        Tariff tariff = tariffRepository.getById(tariffId);
        tariff.setStatus(tariffStatusService.getById(statusId));
        return tariffRepository.save(tariff);
    }

///********************! Connected Transmitter !********************

    public Long connectedTransmitterPageCount (Long transmitterId){
        Long nConnectedTransmitter = tariffRepository.countConnectedTransmitterById(transmitterId);
        Long nPage = nConnectedTransmitter / ROW_COUNT + (nConnectedTransmitter % ROW_COUNT == 0 ? 0 : 1);
        return nPage == 0? nPage + 1 : nPage;
    }

    public List<Transmitter> connectedTransmitterListByNumberPageListAndTariffId (Long numberPageList, Long tariffId){
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

    public Long countPageContractByTariffId(Long tariffId){
        Long nConnectedTransmitter = contractService.countContractByTariffId(tariffId);
        Long nPage = nConnectedTransmitter / ROW_COUNT + (nConnectedTransmitter % ROW_COUNT == 0 ? 0 : 1);
        return nPage == 0? nPage + 1 : nPage;
    }

    public void terminateContractById(Long contractId){
        contractService.terminateContractById(contractId);
    }


///********************! Active tariffs with turned on  transmitters by available address !********************


    public List<Tariff> selectByLimitOffsetAndAvailableAddressIdAndTurnedOnTransmitterAndActiveTariff(
            Long numberPage, Long availableAddressId){
        return tariffRepository.selectByLimitOffsetAndAvailableAddressIdAndTurnedOnTransmitterAndActiveTariff(
                ROW_COUNT, (numberPage - 1) * ROW_COUNT, availableAddressId);
    }

    public Long countByAvailableAddressIdAndTurnedOnTransmitterAndActiveTariff(Long availableAddressId){
        return tariffRepository.countByAvailableAddressIdAndTransmitterStatusAndTariffStatus(
                availableAddressId, "turned on", "active");
    }

    public Long countPageByAvailableAddressIdAndTurnedOnTransmitterAndActiveTariff(Long availableAddressId){
        Long nRow = tariffRepository.countByAvailableAddressIdAndTransmitterStatusAndTariffStatus(
                availableAddressId, "turned on", "active");
        Long nPage = nRow / ROW_COUNT + (nRow % ROW_COUNT == 0 ? 0 : 1);
        return nPage == 0? nPage + 1 : nPage;
    }}
