package ncp.service.implementations;

import ncp.model.Address;
import ncp.model.Transmitter;
import ncp.repository.TransmitterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

import static ncp.config.ProjectConstants.ROW_COUNT;

@Service
public class TransmitterService {
    @Autowired
    private TransmitterRepository transmitterRepository;
    @Autowired
    private TransmitterStatusService transmitterStatusService;
    @Autowired
    private AddressServiceImp addressService;

    public Long pageCount(){
        Long nTransmitter = transmitterRepository.count();
        Long nPage = nTransmitter / ROW_COUNT + (nTransmitter % ROW_COUNT == 0 ? 0 : 1);
        return nPage == 0? nPage + 1 : nPage;
    }

    public Long availableAddressPageCount (Long availableAddressId){
        Long nAvailableAddress = transmitterRepository.countAvailableAddressById(availableAddressId);
        Long nPage = nAvailableAddress / ROW_COUNT + (nAvailableAddress % ROW_COUNT == 0 ? 0 : 1);
        return nPage == 0? nPage + 1 : nPage;
    }

    public List<Transmitter> transmitterListByNumberPageList (Long numberPageList){
        return transmitterRepository.selectByLimitOffset(ROW_COUNT, (numberPageList-1)*ROW_COUNT);
    }

    public List<Address> availableAddressListByNumberPageListAndTransmitterId (Long numberPageList, Long transmitterId){
        return addressService.availableAddressListByNumberPageListAndTransmitterId (numberPageList, transmitterId);
    }

    public Transmitter saveNew (String description){
        Transmitter transmitter = new Transmitter();
        transmitter.setDescription(description);
        transmitter.setStatus(transmitterStatusService.getById(1L));
        transmitter.setAvailableAddresses(new HashSet<>());
        return transmitterRepository.save(transmitter);
    }

    public Transmitter save (Transmitter transmitter){
        return transmitterRepository.save(transmitter);
    }

    public Transmitter setStatus (Long transmitterId, Long statusId){
        Transmitter transmitter = transmitterRepository.getById(transmitterId);
        transmitter.setStatus(transmitterStatusService.getById(statusId));
        return transmitterRepository.save(transmitter);
    }

    public Transmitter setDescription (Long transmitterId, String description){
        Transmitter transmitter = transmitterRepository.getById(transmitterId);
        transmitter.setDescription(description);
        return transmitterRepository.save(transmitter);
    }

    public Transmitter getById (Long id){
        return transmitterRepository.getById(id);
    }

    public Transmitter setAddress (Long transmitterId, Long addressId){
        Transmitter transmitter = transmitterRepository.getById(transmitterId);
        transmitter.setAddress(addressService.getById(addressId));
        return transmitterRepository.save(transmitter);
    }
    public void addAvailableAddress (Long transmitterId, Long addressId){
        transmitterRepository.addAvailableAddressByTransmitterIdAndAddressId(transmitterId, addressId);
    }

    public void deleteById(Long id){
        transmitterRepository.deleteById(id);
    }

    public void removeAvailableAddressByTransmitterIdAndAddressId(Long tId, Long aId){
        transmitterRepository.removeAvailableAddressByTransmitterIdAndAddressId(tId, aId);
    }

    public List<Address> searchAddressByAddress(Address address, Long numberPage){
        return addressService.searchAddressLikeAddress(address, numberPage);
    }

    public List<Address> searchAddressByAddressUnconnectedToTransmitterId(
            Address address, Long numberPage, Long transmitterId){
        return addressService.searchAddressLikeAddressUnconnectedToTransmitterId(address, numberPage, transmitterId);
    }

    public List<Transmitter> connectedTransmitterListByNumberPageListAndTransmitterId(Long numberPageList, Long tariffId){
        return transmitterRepository.selectConnectedTransmitterByLimitOffsetAndId(
                tariffId
                , ROW_COUNT
                , (numberPageList-1)*ROW_COUNT
        );
    }

///********************! Search connectable Transmitters !********************

    public List<Transmitter> searchTransmitterByTransmitterAvailableAddressIdWithoutConnectedTransmitterId(
            Address address, Long numberPage, Long tariffId){
        return transmitterRepository.searchTransmitterByTransmitterAvailableAddressIdWithoutConnectedTransmitterId(

                (address.getCountry() == null || address.getCountry().isBlank()
                        ? "%" : address.getCountry())
                , (address.getRegion() == null || address.getRegion().isBlank()
                        ? "%" : address.getRegion())
                , (address.getCity() == null || address.getCity().isBlank()
                        ? "%" : address.getCity())
                , (address.getStreet() == null || address.getStreet().isBlank()
                        ? "%" : address.getStreet())
                , (address.getHouse() == null ? "%" : address.getHouse().toString())
                , (address.getBuilding() == null ? "%" : address.getBuilding().toString())
                , (address.getFlat() == null ? "%" : address.getFlat().toString())

                , ROW_COUNT, ROW_COUNT*(numberPage-1), tariffId
        );

    }
}
