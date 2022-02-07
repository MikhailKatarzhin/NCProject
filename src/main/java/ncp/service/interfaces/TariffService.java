package ncp.service.interfaces;

import ncp.model.Address;
import ncp.model.Contract;
import ncp.model.Tariff;
import ncp.model.Transmitter;
import org.springframework.ui.ModelMap;

import java.util.List;

public interface TariffService {

///********************! Tariff !********************

    long pageCount ();

    List<Tariff> tariffListByNumberPageList (long numberPageList);

    List<Tariff> ownTariffListByNumberPageList (long numberPageList, Long userId);

    ModelMap validateNewTariff(Tariff tariff, ModelMap model);

    Tariff saveNew(Tariff tariff);

    void deleteById(long id);

    Tariff getById(long id);

    ///********************! Tariff sets !********************

    Tariff setDescription (long tariffId, String description);

    Tariff setPrice (long tariffId, long price);

    Tariff setStatus (long tariffId, long statusId);

///********************! Connected Transmitter !********************

    long connectedTransmitterPageCount (long transmitterId);

    List<Transmitter> connectedTransmitterListByNumberPageListAndTariffId (long numberPageList, Long tariffId);

    void  addConnectedTransmitter(Long tariffId, Long transmitterId);

    void removeConnectedTransmitterByTransmitterIdAndTariffId(Long transmitterId, Long tariffId);

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
