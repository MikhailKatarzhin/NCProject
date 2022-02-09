package ncp.service.interfaces;

import ncp.model.Address;
import ncp.model.Contract;
import ncp.model.Tariff;
import ncp.model.Transmitter;
import org.springframework.ui.ModelMap;

import java.util.List;

public interface TariffService {

///********************! Tariff !********************

    Long pageCount ();

    List<Tariff> tariffListByNumberPageList (Long numberPageList);

    List<Tariff> ownTariffListByNumberPageList (Long numberPageList, Long userId);

    ModelMap validateNewTariff(Tariff tariff, ModelMap model);

    Tariff saveNew(Tariff tariff);

    void deleteById(Long id);

    Tariff getById(Long id);

    ///********************! Tariff sets !********************

    Tariff setDescription (Long tariffId, String description);

    Tariff setPrice (Long tariffId, Long price);

    Tariff setStatus (Long tariffId, Long statusId);

///********************! Connected Transmitter !********************

    Long connectedTransmitterPageCount (Long transmitterId);

    List<Transmitter> connectedTransmitterListByNumberPageListAndTariffId (Long numberPageList, Long tariffId);

    void  addConnectedTransmitter(Long tariffId, Long transmitterId);

    void removeConnectedTransmitterByTransmitterIdAndTariffId(Long transmitterId, Long tariffId);

    Long countConnectedAddressByTariffId(Long tariffId);

///********************! Search connectable Transmitters !********************

    List<Transmitter> searchTransmitterByTransmitterAvailableAddressIdWithoutConnectedTransmitterId(
            Address address, Long numberPage, Long tariffId);

///********************! Signed Contracts !********************

    List<Contract> signedContractListsByTariffId(Long tariffId, Long numberPageList);

    Long countPageContractByTariffId(Long tariffId);

    void terminateContractById(Long contractId);


///********************! Active tariffs with turned on  transmitters by available address !********************


    List<Tariff> selectByLimitOffsetAndAvailableAddressIdAndTurnedOnTransmitterAndActiveTariff(
            Long numberPage, Long availableAddressId);

    Long countByAvailableAddressIdAndTurnedOnTransmitterAndActiveTariff(Long availableAddressId);

    Long countPageByAvailableAddressIdAndTurnedOnTransmitterAndActiveTariff(Long availableAddressId);
}
