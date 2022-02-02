package ncp.service;

import ncp.model.Address;
import ncp.repository.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import java.util.List;

import static ncp.config.ProjectConstants.ROW_COUNT;

@Service
public class AddressService {
    @Autowired
    AddressRepository addressRepository;

    public boolean addressExists(Address address) {
        return addressRepository.countByFlatAndBuildingAndHouseAndStreetAndCityAndRegionAndCountry(
                address.getFlat(), address.getBuilding(), address.getHouse(), address.getStreet()
                , address.getCity(), address.getRegion(), address.getCountry()) != 0;
    }

    public long pageCount(){
        long nPage=addressRepository.count()/ROW_COUNT + (addressRepository.count()%ROW_COUNT == 0? 0:1);
        return nPage == 0? nPage+1 : nPage;
    }

    public List<Address> addressListByNumberPageList(long numberPageList){
        return addressRepository.selectByLimitOffset(ROW_COUNT, (numberPageList-1)*ROW_COUNT);
    }

    public Address save(Address address){
        return addressRepository.save(address);
    }

    public void deleteById(long id){
        addressRepository.deleteById(id);
    }

    public boolean isExistsFlats(Address address){
        return 0 < addressRepository.isExistsFlats(address.getCountry(), address.getRegion(), address.getCity()
                , address.getStreet(), address.getHouse(), address.getBuilding());
    }
    public boolean isExistsBuildings(Address address){
        return 0 < addressRepository.isExistsBuildings(address.getCountry(), address.getRegion(), address.getCity()
        , address.getStreet(), address.getHouse());
    }
    public boolean isExistsStreets(Address address){
        return 0 < addressRepository.isExistsStreets(address.getCountry(), address.getRegion(), address.getCity());
    }
    public boolean isExistsRegions(Address address){
        return 0 < addressRepository.isExistsRegions(address.getCountry());
    }

    public ModelMap addNewAddress(Address address, ModelMap model){

        if (address.getCountry().isEmpty() || address.getCountry()==null){
            model.addAttribute("CountryRequiredError", "Country is required");
            return model;
        }
        if (isExistsRegions(address)){
            model.addAttribute("RegionRequiredError", "Region is required");
            return model;
        }
        if (address.getCity().isEmpty() || address.getCity()==null){
            model.addAttribute("CityRequiredError", "City is required");
            return model;
        }
        if (isExistsStreets(address)){
            model.addAttribute("StreetRequiredError", "Street is required");
            return model;
        }
        if (address.getHouse()==null){
            model.addAttribute("HouseRequiredError", "House is required");
            return model;
        }
        if (isExistsBuildings(address)){
            model.addAttribute("BuildingRequiredError", "Building is required");
            return model;
        }
        if (isExistsFlats(address)){
            model.addAttribute("FlatRequiredError", "Flat is required");
            return model;
        }
        if (addressExists(address)){
            model.addAttribute("addressExistsError", "Address already exists");
            return model;
        }
        save(address);
        return null;
    }
}
