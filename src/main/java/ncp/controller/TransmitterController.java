package ncp.controller;

import ncp.model.Address;
import ncp.model.Transmitter;
import ncp.service.TransmitterService;
import ncp.service.TransmitterStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/transmitter")
public class TransmitterController {
    @Autowired
    private TransmitterService transmitterService;
    @Autowired
    private TransmitterStatusService transmitterStatusService;
    @GetMapping
    public String management(){
        return "redirect:/transmitter/list/1";
    }

    @GetMapping("/list/{numberPageList}")
    public String managementByOffset(@PathVariable Long numberPageList, ModelMap model){
        if (numberPageList < 1L)
            return "redirect:/transmitter/list/1";
        long nPage=transmitterService.pageCount();
        if (numberPageList > nPage)
            return "redirect:/transmitter/list/"+nPage;
        model.addAttribute("nPage", nPage);
        model.addAttribute("currentPage", numberPageList);
        List<Transmitter> transmitterList =transmitterService.transmitterListByNumberPageList(numberPageList);
        model.addAttribute("transmitters", transmitterList);
        return "transmitter/transmitter_management";
    }

    @GetMapping("/to_page")
    public String toPage(@RequestParam("toPage") Long toPage){
        return "redirect:/transmitter/list/"+toPage;
    }

    @GetMapping("/list/{numberPageList}/next_page")
    public String nextPage(@PathVariable Long numberPageList){
        return "redirect:/transmitter/list/"+(numberPageList+1L);
    }

    @GetMapping("/list/{numberPageList}/preview_page")
    public String previewPage(@PathVariable Long numberPageList){
        return "redirect:/transmitter/list/"+(numberPageList-1L);
    }

    @GetMapping("/last_page")
    public String lastPage(){
        return "redirect:/transmitter/list/"+transmitterService.pageCount();
    }

    @PostMapping("/add")
    public String transmitterAdd(@RequestParam String description){
        transmitterService.saveNew(description);
        return "redirect:/transmitter/list/1";
    }

    @PostMapping("/list/{numberPageList}/{id}/remove")
    public String removeById(@PathVariable Long numberPageList, @PathVariable Long id){
        transmitterService.deleteById(id);
        return "redirect:/transmitter/list/"+numberPageList;
    }

    @GetMapping("/setup/{id}")
    public String setupById(@PathVariable Long id, ModelMap model){
        model.addAttribute("transmitter", transmitterService.getById(id));
        model.addAttribute("statusList", transmitterStatusService.getAll());
        return "redirect:/transmitter/setup/"+id+"/list/1";
    }

    @GetMapping("/setup/{id}/list/{availableAddressPage}")
    public String setupByIdAndPage(@PathVariable Long id, @PathVariable Long availableAddressPage, ModelMap model){
        if (availableAddressPage < 1L)
            return "redirect:/transmitter/setup/"+id+"/1";
        long nPage=transmitterService.availableAddressPageCount(id);
        if (availableAddressPage > nPage)
            return "redirect:/transmitter/list/"+nPage;
        model.addAttribute("nPage", nPage);
        List<Address> addressList = transmitterService.availableAddressListByNumberPageListAndTransmitterId(nPage, id);
        model.addAttribute("addresses", addressList);
        model.addAttribute("availableAddressPage", availableAddressPage);
        model.addAttribute("transmitter", transmitterService.getById(id));
        model.addAttribute("statusList", transmitterStatusService.getAll());
        return "transmitter/setup";
    }

    @GetMapping("/setup/{id}/to_page")
    public String toAvailableAddressPage(@RequestParam("toPage") Long toPage, @PathVariable Long id){
        return "redirect:/transmitter/setup/"+id+"/list/"+toPage;
    }

    @GetMapping("/setup/{id}/list/{availableAddressPage}/next_page")
    public String nextAvailableAddressPage(@PathVariable Long id, @PathVariable Long availableAddressPage){
        return "redirect:/transmitter/setup/"+id+"/list/"+(availableAddressPage+1L);
    }

    @GetMapping("/setup/{id}/list/{availableAddressPage}/preview_page")
    public String previewAvailableAddressPage(@PathVariable Long id, @PathVariable Long availableAddressPage){
        return "redirect:/transmitter/setup/"+id+"/list/"+(availableAddressPage-1L);
    }

    @GetMapping("/setup/{id}/last_page")
    public String lastAvailableAddressPage(@PathVariable Long id){
        return "redirect:/transmitter/setup/"+id+"/list/"+transmitterService.availableAddressPageCount(id);
    }

    @PostMapping("/setup/{id}/{availableAddressPage}/status")
    public String setStatusByTransmitterId(
            @PathVariable Long id
            , @PathVariable Long availableAddressPage
            , @RequestParam long statusSelect){
        transmitterService.setStatus(id, statusSelect);
        return "redirect:/transmitter/setup/"+id+"/list/"+availableAddressPage;
    }

    @PostMapping("/setup/{id}/{availableAddressPage}/removeAvailableAddress/{idAA}")
    public String removeAvailableAddressByTransmitterIdAndAddressId(@PathVariable Long id
            , @PathVariable Long availableAddressPage, @PathVariable Long idAA){
        transmitterService.removeAvailableAddressByTransmitterIdAndAddressId(id, idAA);
        return "redirect:/transmitter/setup/"+id+"/list/"+availableAddressPage;
    }
}
