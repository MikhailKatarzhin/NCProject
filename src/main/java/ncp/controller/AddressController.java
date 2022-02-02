package ncp.controller;

import ncp.model.Address;
import ncp.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/address")
public class AddressController {
    @Autowired
    private AddressService addressService;

    @GetMapping
    public String managementByOffset(){
        return "redirect:/address/list/1";
    }

    @GetMapping("/list/{numberPageList}")
    public String managementByOffset(@PathVariable Long numberPageList, ModelMap model){
        if (numberPageList < 1L)
            return "redirect:/address/list/1";
        long nPage=addressService.pageCount();
        if (numberPageList > nPage)
            return "redirect:/address/list/"+nPage;
        model.addAttribute("nPage", nPage);
        model.addAttribute("currentPage", numberPageList);
        List<Address> addressList =addressService.addressListByNumberPageList(numberPageList);
        model.addAttribute("addresses", addressList);
        return "address/address_management";
    }

    @GetMapping("/to_page")
    public String toPage(@RequestParam("toPage") Long toPage){
        return "redirect:/address/list/"+toPage;
    }

    @GetMapping("/list/{numberPageList}/next_page")
    public String nextPage(@PathVariable Long numberPageList, ModelMap model){
        return "redirect:/address/list/"+(numberPageList+1L);
    }

    @GetMapping("/list/{numberPageList}/preview_page")
    public String previewPage(@PathVariable Long numberPageList, ModelMap model){
        return "redirect:/address/list/"+(numberPageList-1L);
    }

    @GetMapping("/last_page")
    public String lastPage(){
        return "redirect:/address/list/"+addressService.pageCount();
    }

    @GetMapping("/add")
    public String addressAddForm(ModelMap model){
        model.addAttribute("address", new Address());
        return "address/add";
    }

    @PostMapping("/add")
    public String addressAdd(@ModelAttribute Address address, ModelMap model){

        model = addressService.addNewAddress(address, model);
        if (model != null)
            return "address/add";
        return "redirect:/address/list/1";
    }

    @PostMapping("/list/{numberPageList}/{id}/remove")
    public String removeById(@PathVariable Long numberPageList, @PathVariable Long id){
        addressService.deleteById(id);
        return "redirect:/address/list/"+numberPageList;
    }
}
