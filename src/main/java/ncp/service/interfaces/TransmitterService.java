package ncp.service.interfaces;

import ncp.model.Address;
import ncp.model.Transmitter;
import java.util.List;

public interface TransmitterService {

    Long pageCount();

    Long availableAddressPageCount (Long availableAddressId);

    Long countAvailableAddressByTransmitterId(Long TransmitterId);

    List<Transmitter> transmitterListByNumberPageList (Long numberPageList);

    List<Address> availableAddressListByNumberPageListAndTransmitterId (Long numberPageList, Long transmitterId);

    Transmitter saveNew (String description);

    Transmitter save (Transmitter transmitter);

    Transmitter setStatus (Long transmitterId, Long statusId);

    Transmitter setDescription (Long transmitterId, String description);

    Transmitter getById (Long id);

    Transmitter setAddress (Long transmitterId, Long addressId);
    
    void addAvailableAddress (Long transmitterId, Long addressId);

    void deleteById(Long id);

    void removeAvailableAddressByTransmitterIdAndAddressId(Long tId, Long aId);

    List<Address> searchAddressByAddress(Address address, Long numberPage);

    List<Address> searchAddressByAddressUnconnectedToTransmitterId(
            Address address, Long numberPage, Long transmitterId);

    List<Transmitter> connectedTransmitterListByNumberPageListAndTransmitterId(Long numberPageList, Long tariffId);

///********************! Search connectable Transmitters !********************

    List<Transmitter> searchTransmitterByTransmitterAvailableAddressIdWithoutConnectedTransmitterId(
            Address address, Long numberPage, Long tariffId);
}
