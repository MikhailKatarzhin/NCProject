package ncp.service.interfaces;

import ncp.model.Address;
import org.springframework.ui.ModelMap;

import java.util.List;

public interface AddressService {

    boolean addressExists(Address address);

    Long pageCount();

    List<Address> addressListByNumberPageList(Long numberPageList);

    Address save(Address address);

    Address getById(Long id);

    void deleteById(Long id);

    boolean isExistsFlats(Address address);

    boolean isExistsBuildings(Address address);

    boolean isExistsStreets(Address address);

    boolean isExistsRegions(Address address);

    List<Address> searchAddressLikeAddress(Address address, Long numberPage);

    List<Address> searchAddressLikeAddressUnconnectedToTransmitterId(
            Address address, Long numberPage, Long transmitterId);

    void addNewAddresses(Address address);

    List<Address> availableAddressListByNumberPageListAndTransmitterId(Long numberPageList, Long transmitterId);
}
