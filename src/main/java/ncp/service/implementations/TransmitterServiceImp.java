package ncp.service.implementations;

import ncp.model.Address;
import ncp.model.Transmitter;
import ncp.repository.TransmitterRepository;
import ncp.service.interfaces.TransmitterService;
import ncp.service.interfaces.TransmitterStatusService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

import static ncp.config.ProjectConstants.ROW_COUNT;

@Service
public class TransmitterServiceImp implements TransmitterService {

    private final static Logger logger = LoggerFactory.getLogger(TransmitterServiceImp.class);

    private final TransmitterStatusService transmitterStatusService;
    private final TransmitterRepository transmitterRepository;
    private final AddressServiceImp addressService;

    @Autowired
    public TransmitterServiceImp(TransmitterRepository transmitterRepository
            , TransmitterStatusService transmitterStatusService, AddressServiceImp addressService) {
        this.transmitterStatusService = transmitterStatusService;
        this.transmitterRepository = transmitterRepository;
        this.addressService = addressService;
    }

    public Long pageCount() {
        Long nTransmitter = transmitterRepository.count();
        Long nPage = nTransmitter / ROW_COUNT + (nTransmitter % ROW_COUNT == 0 ? 0 : 1);
        return nPage == 0 ? nPage + 1 : nPage;
    }

    public Long availableAddressPageCount(Long TransmitterId) {
        Long nAvailableAddress = transmitterRepository.countAvailableAddressByTransmitterId(TransmitterId);
        Long nPage = nAvailableAddress / ROW_COUNT + (nAvailableAddress % ROW_COUNT == 0 ? 0 : 1);
        return nPage == 0 ? nPage + 1 : nPage;
    }

    public Long countAvailableAddressByTransmitterId(Long TransmitterId) {
        return transmitterRepository.countAvailableAddressByTransmitterId(TransmitterId);
    }

    public List<Transmitter> transmitterListByNumberPageList(Long numberPageList) {
        return transmitterRepository.selectByLimitOffset(ROW_COUNT, (numberPageList - 1) * ROW_COUNT);
    }

    public List<Address> availableAddressListByNumberPageListAndTransmitterId(Long numberPageList, Long transmitterId) {
        return addressService.availableAddressListByNumberPageListAndTransmitterId(numberPageList, transmitterId);
    }

    public Transmitter saveNew(String description) {
        Transmitter transmitter = new Transmitter();
        transmitter.setDescription(description);
        transmitter.setStatus(transmitterStatusService.getById(1L));
        transmitter.setAvailableAddresses(new HashSet<>());
        transmitter = transmitterRepository.save(transmitter);
        logger.info("Added new transmitter [id:{}]", transmitter.getId());
        return transmitter;
    }

    public Transmitter save(Transmitter transmitter) {
        return transmitterRepository.save(transmitter);
    }

    public Transmitter setStatus(Long transmitterId, Long statusId) {
        Transmitter transmitter = transmitterRepository.getById(transmitterId);
        transmitter.setStatus(transmitterStatusService.getById(statusId));
        transmitter = transmitterRepository.save(transmitter);
        logger.info("Status of transmitter [id:{}] set to {}", transmitter.getId(), transmitter.getStatus().getName());
        return transmitter;
    }

    public Transmitter setDescription(Long transmitterId, String description) {
        Transmitter transmitter = transmitterRepository.getById(transmitterId);
        transmitter.setDescription(description);
        transmitter = transmitterRepository.save(transmitter);
        logger.info("Changed description of transmitter [id:{}]", transmitter.getId());
        return transmitter;
    }

    public Transmitter getById(Long id) {
        return transmitterRepository.getById(id);
    }

    public Transmitter setAddress(Long transmitterId, Long addressId) {
        Transmitter transmitter = transmitterRepository.getById(transmitterId);
        transmitter.setAddress(addressService.getById(addressId));
        transmitter = transmitterRepository.save(transmitter);
        logger.info("Changed address of transmitter [id:{}] to address id:{}"
                , transmitterId, addressId);
        return transmitter;
    }

    public void addAvailableAddress(Long transmitterId, Long addressId) {
        transmitterRepository.addAvailableAddressByTransmitterIdAndAddressId(transmitterId, addressId);
    }

    public void deleteById(Long id) {
        transmitterRepository.deleteById(id);
        logger.info("Deleted transmitter [id:{}]", id);
    }

    public void removeAvailableAddressByTransmitterIdAndAddressId(Long tId, Long aId) {
        transmitterRepository.removeAvailableAddressByTransmitterIdAndAddressId(tId, aId);
    }

    public List<Address> searchAddressByAddress(Address address, Long numberPage) {
        return addressService.searchAddressLikeAddress(address, numberPage);
    }

    public List<Address> searchAddressByAddressUnconnectedToTransmitterId(
            Address address, Long numberPage, Long transmitterId) {
        return addressService.searchAddressLikeAddressUnconnectedToTransmitterId(address, numberPage, transmitterId);
    }

    public List<Transmitter> connectedTransmitterListByNumberPageListAndTransmitterId(Long numberPageList, Long tariffId) {
        return transmitterRepository.selectConnectedTransmitterByLimitOffsetAndId(
                tariffId
                , ROW_COUNT
                , (numberPageList - 1) * ROW_COUNT
        );
    }

///********************! Search connectable Transmitters !********************

    public List<Transmitter> searchTransmitterByTransmitterAvailableAddressIdWithoutConnectedTransmitterId(
            Address address, Long numberPage, Long tariffId) {
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

                , ROW_COUNT, ROW_COUNT * (numberPage - 1), tariffId
        );

    }
}
