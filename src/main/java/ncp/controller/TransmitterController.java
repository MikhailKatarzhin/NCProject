package ncp.controller;

import ncp.controller.paging.AbstractSecondaryPagingController;
import ncp.model.Address;
import ncp.model.Transmitter;
import ncp.service.interfaces.TransmitterService;
import ncp.service.interfaces.TransmitterStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/transmitter")
public class TransmitterController extends AbstractSecondaryPagingController {
    @Autowired
    private TransmitterService transmitterService;
    @Autowired
    private TransmitterStatusService transmitterStatusService;

///********************! Transmitter management !********************

    @GetMapping
    public String management(){
        return firstPage();
    }

    @PostMapping("/add")
    public String transmitterAdd(@RequestParam String description){
        transmitterService.saveNew(description);
        return firstPage();
    }

    @PostMapping("/list/{numberPageList}/{id}/remove")
    public String removeById(@PathVariable Long numberPageList, @PathVariable Long id){
        transmitterService.deleteById(id);
        return toPage(numberPageList);
    }

    @GetMapping("/list/{numberPageList}")
    public String managementByOffset(@PathVariable Long numberPageList, ModelMap model){
        if (numberPageList < 1L)
            return firstPage();
        Long nPage=transmitterService.pageCount();
        if (numberPageList > nPage)
            return lastPage();
        model.addAttribute("nPage", nPage);
        model.addAttribute("currentPage", numberPageList);
        List<Transmitter> transmitterList =transmitterService.transmitterListByNumberPageList(numberPageList);
        model.addAttribute("transmitters", transmitterList);
        return "transmitter/management";
    }

///********************! Pagination transmitters !********************

    @Override
    protected Long primaryPageCount() {
        return transmitterService.pageCount();
    }

    @Override
    protected String getPrimaryPrefix() {
        return "/transmitter";
    }

///********************! Setup of Transmitter !********************

    @GetMapping("/setup/{id}")
    public String setupById(@PathVariable Long id){
        return firstSecondaryPage(id);
    }

    @GetMapping("/setup/{id}/list/{availableAddressPage}")
    public String setupByIdAndPage(@PathVariable Long id, @PathVariable Long availableAddressPage, ModelMap model){
        if (availableAddressPage < 1L)
            return firstSecondaryPage(id);
        Long nPage=transmitterService.availableAddressPageCount(id);
        if (availableAddressPage > nPage)
            return lastSecondaryPage(id);
        model.addAttribute("nPage", nPage);
        List<Address> addressList = transmitterService.availableAddressListByNumberPageListAndTransmitterId(availableAddressPage, id);
        model.addAttribute("addresses", addressList);
        model.addAttribute("availableAddressPage", availableAddressPage);
        model.addAttribute("transmitter", transmitterService.getById(id));
        model.addAttribute("statusList", transmitterStatusService.getAll());
        return "transmitter/setup";
    }

    @PostMapping("/setup/{id}/{availableAddressPage}/status")
    public String setStatusByTransmitterId(
            @PathVariable Long id
            , @PathVariable Long availableAddressPage
            , @RequestParam Long statusSelectId){
        transmitterService.setStatus(id, statusSelectId);
        return toSecondaryPage(availableAddressPage, id);
    }

    @PostMapping("/setup/{id}/{availableAddressPage}/description")
    public String setDescriptionByTransmitterId(
            @PathVariable Long id
            , @PathVariable Long availableAddressPage
            , @RequestParam String description){
        transmitterService.setDescription(id, description);
        return toSecondaryPage(availableAddressPage, id);
    }

    @PostMapping("/setup/{id}/address/{addressId}")
    public String setAddress(@PathVariable Long id, @PathVariable Long addressId){
        transmitterService.setAddress(id, addressId);
        return setupById(id);
    }

///********************! Search addresses !********************

    @GetMapping("/setup/{id}/address")
    public String selectAddress(@PathVariable Long id, ModelMap model){
        return selectAddress(id, new Address(), model);
    }

    @GetMapping("/setup/{id}/address/list")
    public String selectAddress(@PathVariable Long id, Address address, ModelMap model){
        if (address==null)
            address = new Address();
        model.addAttribute("transmitterId", id);
        model.addAttribute("searchAddress", address);
        model.addAttribute("addresses", transmitterService.searchAddressByAddress(address, 1L));
        return "transmitter/setup_address";
    }

    @GetMapping("/setup/{id}/availableAddress")
    public String addAvailableAddress(@PathVariable Long id, ModelMap model){
        return addAvailableAddress(id, new Address(), model);
    }

    @GetMapping("/setup/{id}/availableAddress/list")
    public String addAvailableAddress(@PathVariable Long id, Address address, ModelMap model){
        if (address==null)
            address = new Address();
        model.addAttribute("transmitterId", id);
        model.addAttribute("searchAddress", address);
        model.addAttribute("addresses"
                , transmitterService.searchAddressByAddressUnconnectedToTransmitterId(address, 1L, id));
        return "transmitter/add_available_address";
    }

///********************! Pagination available addresses !********************

    @Override
    protected Long secondaryPageCount(Long id) {
        return transmitterService.availableAddressPageCount(id);
    }

    @Override
    protected String getSecondaryPrefix() {
        return "/setup";
    }

///********************! Available address management !********************

    @PostMapping("/setup/{id}/availableAddress/{addressId}")
    public String addAvailableAddress(@PathVariable Long id, @PathVariable Long addressId
            , Address address, ModelMap model){
        transmitterService.addAvailableAddress(id, addressId);
        return addAvailableAddress(id,address,model);
    }

    @PostMapping("/setup/{id}/{availableAddressPage}/removeAvailableAddress/{idAA}")
    public String removeAvailableAddressByTransmitterIdAndAddressId(@PathVariable Long id
            , @PathVariable Long availableAddressPage, @PathVariable Long idAA){
        transmitterService.removeAvailableAddressByTransmitterIdAndAddressId(id, idAA);
        return toSecondaryPage(availableAddressPage, id);
    }

}
