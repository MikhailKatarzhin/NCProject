package ncp.service.implementations;

import ncp.model.Address;
import ncp.repository.AddressRepository;
import ncp.service.interfaces.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static ncp.config.ProjectConstants.ROW_COUNT;

@Service
public class AddressServiceImp implements AddressService {

    private final AddressRepository addressRepository;

    @Autowired
    public AddressServiceImp(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    public boolean addressExists(Address address) {
        if (address.getFlat() == null)
            return addressRepository.countByBuildingAndHouseAndStreetAndCityAndRegionAndCountryFlatIsNull(
                    address.getBuilding(), address.getHouse()
                    , address.getStreet() == null ? "" : address.getStreet()
                    , address.getCity()
                    , address.getRegion() == null ? "" : address.getRegion()
                    , address.getCountry()) != 0;
        return addressRepository.countByFlatAndBuildingAndHouseAndStreetAndCityAndRegionAndCountry(
                address.getFlat(), address.getBuilding(), address.getHouse()
                , address.getStreet() == null ? "" : address.getStreet()
                , address.getCity()
                , address.getRegion() == null ? "" : address.getRegion()
                , address.getCountry()) != 0;
    }

    public Long pageCount() {
        long nPage = addressRepository.count() / ROW_COUNT + (addressRepository.count() % ROW_COUNT == 0 ? 0 : 1);
        return nPage == 0 ? nPage + 1 : nPage;
    }

    public List<Address> addressListByNumberPageList(Long numberPageList) {
        return addressRepository.selectByLimitOffset(ROW_COUNT, (numberPageList - 1) * ROW_COUNT);
    }

    public Address save(Address address) {
        return addressRepository.save(address);
    }

    public Address getById(Long id) {
        return addressRepository.getById(id);
    }

    public void deleteById(Long id) {
        addressRepository.deleteById(id);
    }

    public boolean isExistsFlats(Address address) {
        return 0 < addressRepository.countCountyRegionCityStreetHouseBuildingsWithFlats(
                address.getCountry(), address.getRegion(), address.getCity()
                , address.getStreet(), address.getHouse(), address.getBuilding());
    }

    public boolean isExistsBuildings(Address address) {
        return 0 < addressRepository.countCountyRegionCityStreetHouseWithBuildings(
                address.getCountry(), address.getRegion(), address.getCity(), address.getStreet(), address.getHouse());
    }

    public boolean isExistsStreets(Address address) {
        return 0 < addressRepository.countCountyRegionCityWithStreets(
                address.getCountry(), address.getRegion(), address.getCity());
    }

    public boolean isExistsRegions(Address address) {
        return 0 < addressRepository.countCountryWithRegions(address.getCountry());
    }

    public List<Address> searchAddressLikeAddress(Address address, Long numberPage) {
        if (address.getFlat() == null)
            return searchAddressLikeAddressFlatIsNull(address, numberPage);
        return addressRepository.searchAddress(
                "%" + (address.getCountry() == null || address.getCountry().isBlank()
                        ? "" : address.getCountry()) + "%"
                , "%" + (address.getRegion() == null || address.getRegion().isBlank()
                        ? "" : address.getRegion()) + "%"
                , "%" + (address.getCity() == null || address.getCity().isBlank()
                        ? "" : address.getCity()) + "%"
                , "%" + (address.getStreet() == null || address.getStreet().isBlank()
                        ? "" : address.getStreet()) + "%"
                , "%" + (address.getHouse() == null ? "" : address.getHouse().toString()) + "%"
                , "%" + (address.getBuilding() == null ? "" : address.getBuilding().toString()) + "%"
                , "%" + (address.getFlat() == null ? "" : address.getFlat().toString()) + "%"
                , ROW_COUNT, ROW_COUNT * (numberPage - 1));
    }

    public List<Address> searchAddressLikeAddressFlatIsNull(Address address, Long numberPage) {
        return addressRepository.searchAddressLikeAddressFlatIsNull(
                "%" + (address.getCountry() == null || address.getCountry().isBlank()
                        ? "" : address.getCountry()) + "%"
                , "%" + (address.getRegion() == null || address.getRegion().isBlank()
                        ? "" : address.getRegion()) + "%"
                , "%" + (address.getCity() == null || address.getCity().isBlank()
                        ? "" : address.getCity()) + "%"
                , "%" + (address.getStreet() == null || address.getStreet().isBlank()
                        ? "" : address.getStreet()) + "%"
                , "%" + (address.getHouse() == null ? "" : address.getHouse().toString()) + "%"
                , "%" + (address.getBuilding() == null ? "" : address.getBuilding().toString()) + "%"
                , ROW_COUNT, ROW_COUNT * (numberPage - 1));
    }

    public List<Address> searchAddressLikeAddressUnconnectedToTransmitterId(
            Address address, Long numberPage, Long transmitterId) {
        return addressRepository.searchAddressUnconnectedToTransmitterId(
                "%" + (address.getCountry() == null || address.getCountry().isBlank()
                        ? "" : address.getCountry()) + "%"
                , "%" + (address.getRegion() == null || address.getRegion().isBlank()
                        ? "" : address.getRegion()) + "%"
                , "%" + (address.getCity() == null || address.getCity().isBlank()
                        ? "" : address.getCity()) + "%"
                , "%" + (address.getStreet() == null || address.getStreet().isBlank()
                        ? "" : address.getStreet()) + "%"
                , "%" + (address.getHouse() == null ? "" : address.getHouse().toString()) + "%"
                , "%" + (address.getBuilding() == null ? "" : address.getBuilding().toString()) + "%"
                , "%" + (address.getFlat() == null ? "" : address.getFlat().toString()) + "%"
                , ROW_COUNT, ROW_COUNT * (numberPage - 1), transmitterId);
    }

    public void addNewAddresses(Address address){
        for (Long i = address.getFlat(); i > 0 ; i--) {
            if (0 == addressRepository.countByFlatAndBuildingAndHouseAndStreetAndCityAndRegionAndCountry(
                    address.getFlat(),
                    address.getBuilding(),
                    address.getHouse(),
                    address.getStreet(),
                    address.getCity(),
                    address.getRegion(),
                    address.getCountry()
            )) {
                Address tmp = new Address();
                tmp.setFlat(i);
                tmp.setBuilding(address.getBuilding());
                tmp.setHouse(address.getHouse());
                tmp.setStreet(address.getStreet());
                tmp.setCity(address.getCity());
                tmp.setRegion(address.getRegion());
                tmp.setCountry(address.getCountry());
                save(tmp);
            }
        }
    }

    public List<Address> availableAddressListByNumberPageListAndTransmitterId(Long numberPageList, Long transmitterId) {
        return addressRepository.selectAvailableAddressByLimitOffsetAndId(
                transmitterId
                , ROW_COUNT
                , (numberPageList - 1) * ROW_COUNT
        );
    }
}
