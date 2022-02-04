package ncp.service;

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
    private AddressService addressService;

    public long pageCount(){
        long nPage=transmitterRepository.count()/ROW_COUNT + (transmitterRepository.count()%ROW_COUNT == 0? 0:1);
        return nPage == 0? nPage+1 : nPage;
    }

    public long availableAddressPageCount(long id){
        long nPage=transmitterRepository.countAvailableAddressById(id)/ROW_COUNT
                + (transmitterRepository.countAvailableAddressById(id)%ROW_COUNT == 0? 0:1);
        return nPage == 0? nPage+1 : nPage;
    }

    public List<Transmitter> transmitterListByNumberPageList(long numberPageList){
        return transmitterRepository.selectByLimitOffset(ROW_COUNT, (numberPageList-1)*ROW_COUNT);
    }

    public List<Address> availableAddressListByNumberPageListAndTransmitterId(long numberPageList, Long transmitterId){
        return transmitterRepository.selectAvailableAddressByLimitOffsetAndId(
                ROW_COUNT
                , (numberPageList-1)*ROW_COUNT
                , transmitterId
        );
    }

    public Transmitter saveNew(String description){
        Transmitter transmitter = new Transmitter();
        transmitter.setDescription(description);
        transmitter.setStatus(transmitterStatusService.getById(1));
        transmitter.setAvailableAddresses(new HashSet<>());
        return transmitterRepository.save(transmitter);
    }

    public Transmitter save(Transmitter transmitter){
        return transmitterRepository.save(transmitter);
    }

    public Transmitter setStatus(long transmitterId, long statusId){
        Transmitter transmitter = transmitterRepository.getById(transmitterId);
        transmitter.setStatus(transmitterStatusService.getById(statusId));
        return transmitterRepository.save(transmitter);
    }

    public Transmitter setDescription(long transmitterId, String description){
        Transmitter transmitter = transmitterRepository.getById(transmitterId);
        transmitter.setDescription(description);
        return transmitterRepository.save(transmitter);
    }

    public Transmitter getById(long id){
        return transmitterRepository.getById(id);
    }

    public Transmitter setAddress(Long transmitterId, Long addressId){
        Transmitter transmitter = transmitterRepository.getById(transmitterId);
        transmitter.setAddress(addressService.getById(addressId));
        return transmitterRepository.save(transmitter);
    }
    public void addAvailableAddress(Long transmitterId, Long addressId){
        transmitterRepository.addAvailableAddressByTransmitterIdAndAddressId(transmitterId, addressId);
    }

    public void deleteById(long id){
        transmitterRepository.deleteById(id);
    }

    public void removeAvailableAddressByTransmitterIdAndAddressId(long tId, long aId){
        transmitterRepository.removeAvailableAddressByTransmitterIdAndAddressId(tId, aId);
    }

    public List<Address> searchAddressByAddress(Address address, Long numberPage){
        return addressService.searchAddressLikeAddress(address, numberPage);
    }

    public List<Address> searchAddressByAddressUnconnectedToTransmitterId(
            Address address, Long numberPage, Long transmitterId){
        return addressService.searchAddressLikeAddressUnconnectedToTransmitterId(address, numberPage, transmitterId);
    }

}
