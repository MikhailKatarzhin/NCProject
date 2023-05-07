package ncp.controller;

import ncp.controller.paging.AbstractPrimaryPagingController;
import ncp.model.Address;
import ncp.service.interfaces.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/address")
public class AddressController extends AbstractPrimaryPagingController {

    private final AddressService addressService;

    @Autowired
    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

///********************! Address management !********************

    @GetMapping
    public String managementByOffset() {
        return "redirect:/address/list/1";
    }

    @GetMapping("/list/{currentPage}")
    public String managementByOffset(@PathVariable Long currentPage, ModelMap model) {
        if (currentPage < 1L)
            return "redirect:/address/list/1";
        Long nPage = addressService.pageCount();
        if (currentPage > nPage)
            return "redirect:/address/list/" + nPage;
        model.addAttribute("nPage", nPage);
        model.addAttribute("currentPage", currentPage);
        List<Address> addressList = addressService.addressListByNumberPageList(currentPage);
        model.addAttribute("addresses", addressList);
        return "address/management";
    }

    @GetMapping("/add")
    public String addressAddForm(ModelMap model) {
        model.addAttribute("address", new Address());
        return "address/add";
    }

    @PostMapping("/add")
    public String addressAdd(@ModelAttribute Address address, ModelMap model, boolean manyFlats) {
        int modelSize = model.size();
        model = checkAddress(address, model);
        if (model.size() != modelSize){
            return "address/add";
        }
        if (manyFlats)
            return addressesAdd(address);
        addressService.save(address);
        return "redirect:/address/list/1";
    }

    @PostMapping("/adds")
    public String addressesAdd(@ModelAttribute Address address) {
        addressService.addNewAddresses(address);
        return "redirect:/address/list/1";
    }

    @PostMapping("/list/{currentPage}/{id}/remove")
    public String removeById(@PathVariable Long currentPage, @PathVariable Long id) {
        addressService.deleteById(id);
        return "redirect:/address/list/" + currentPage;
    }

///********************! Pagination addresses !********************

    @Override
    protected Long pageCount() {
        return addressService.pageCount();
    }

    @Override
    protected String getPrefix() {
        return "/address";
    }


///********************! Service methods !********************

    private ModelMap checkAddress(Address address, ModelMap model){
        if (address.getCountry().isBlank()) {
            model.addAttribute("CountryError", "Country is required");
        } else if (address.getCountry().length() < 2) {
            model.addAttribute("CountryError", "Country length must be more 1");
        }
        if (address.getRegion().isBlank()) {
            address.setRegion("");
            if (addressService.isExistsRegions(address)) {
                model.addAttribute("RegionError", "Region is required");
            }
        } else if (address.getRegion().length() < 2) {
            model.addAttribute("RegionError", "Region length must be more 1");
        }
        if (address.getCity().isBlank()) {
            model.addAttribute("CityError", "City is required");
        } else if (address.getCity().length() < 2) {
            model.addAttribute("CityError", "City length must be more 1");
        }
        if (address.getStreet().isBlank()) {
            address.setStreet("");
            if (addressService.isExistsStreets(address)) {
                model.addAttribute("StreetError", "Street is required");
            }
        } else if (address.getStreet().length() < 2) {
            model.addAttribute("StreetError", "Street length must be more 1");
        }
        if (address.getHouse() == null) {
            model.addAttribute("HouseError", "House is required");
        } else if (address.getHouse() < 1) {
            model.addAttribute("HouseError", "House number must be more 0");
        }
        if (address.getBuilding() == null) {
            if (addressService.isExistsBuildings(address)) {
                model.addAttribute("BuildingError", "Building is required");
            }
        } else if (address.getBuilding() < 1) {
            model.addAttribute("BuildingError", "Building number must be more 0");
        }
        if (address.getFlat() == null) {
            if (addressService.isExistsFlats(address)) {
                model.addAttribute("FlatError", "Flat is required");
            }
        } else if (address.getFlat() < 1) {
            model.addAttribute("FlatError", "Flat number must be more 0");
        }
        if (addressService.addressExists(address)) {
            model.addAttribute("addressExistsError", "Address already exists");
        }
        return model;
    }
}
