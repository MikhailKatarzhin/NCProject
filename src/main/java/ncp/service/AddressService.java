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

    public Address getById(Long id){
        return addressRepository.getById(id);
    }
    public void deleteById(long id){
        addressRepository.deleteById(id);
    }

    public boolean isExistsFlats(Address address){
        return 0 < addressRepository.countCountyRegionCityStreetHouseBuildingsWithFlats(address.getCountry(), address.getRegion(), address.getCity()
                , address.getStreet(), address.getHouse(), address.getBuilding());
    }
    public boolean isExistsBuildings(Address address){
        return 0 < addressRepository.countCountyRegionCityStreetHouseWithBuildings(address.getCountry(), address.getRegion(), address.getCity()
        , address.getStreet(), address.getHouse());
    }
    public boolean isExistsStreets(Address address){
        return 0 < addressRepository.countCountyRegionCityWithStreets(address.getCountry(), address.getRegion(), address.getCity());
    }
    public boolean isExistsRegions(Address address){
        return 0 < addressRepository.countCountyWithRegions(address.getCountry());
    }


    public List<Address> searchAddressLikeAddress(Address address, Long numberPage){
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
                , ROW_COUNT, ROW_COUNT*(numberPage-1));
    }

    public ModelMap addNewAddress(Address address, ModelMap model){

        if (address.getCountry().isBlank()){
            model.addAttribute("CountryError", "Country is required");
            return model;
        }else if(address.getCountry().length()<2){
            model.addAttribute("CountryError", "Country length must be more 1");
            return model;
        }
        if (isExistsRegions(address) && address.getRegion().isBlank()){
            model.addAttribute("RegionError", "Region is required");
            return model;
        }else if(address.getRegion().length() <2){
            model.addAttribute("RegionError", "Region length must be more 1");
            return model;
        }
        if (address.getCity().isBlank()){
            model.addAttribute("CityError", "City is required");
            return model;
        }else if(address.getCity().length()<2){
            model.addAttribute("CityError", "City length must be more 1");
            return model;
        }
        if (isExistsStreets(address) && address.getStreet().isBlank()){
            model.addAttribute("StreetError", "Street is required");
            return model;
        }else if(address.getStreet().length() <2){
            model.addAttribute("StreetError", "Street length must be more 1");
            return model;
        }
        if (address.getHouse()==null){
            model.addAttribute("HouseError", "House is required");
            return model;
        }else if(address.getHouse()<1){
            model.addAttribute("HouseError", "House number must be more 0");
            return model;
        }
        if (isExistsBuildings(address) && address.getBuilding()==null){
            model.addAttribute("BuildingError", "Building is required");
            return model;
        }else if(address.getBuilding()<1){
            model.addAttribute("BuildingError", "Building number must be more 0");
            return model;
        }
        if (isExistsFlats(address) && address.getFlat()==null){
            model.addAttribute("FlatError", "Flat is required");
            return model;
        }else if(address.getFlat()<1){
            model.addAttribute("FlatError", "Flat number must be more 0");
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
