package ncp.controller;

import ncp.controller.paging.AbstractPrimaryPagingController;
import ncp.model.Address;
import ncp.service.implementations.AddressServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/address")
public class AddressController extends AbstractPrimaryPagingController {

    private final AddressServiceImp addressService;

    @Autowired
    public AddressController(AddressServiceImp addressService){
        this.addressService = addressService;
    }
///********************! Address management !********************

    @GetMapping
    public String managementByOffset(){
        return "redirect:/address/list/1";
    }

    @GetMapping("/list/{currentPage}")
    public String managementByOffset(@PathVariable Long currentPage, ModelMap model){
        if (currentPage < 1L)
            return "redirect:/address/list/1";
        long nPage=addressService.pageCount();
        if (currentPage > nPage)
            return "redirect:/address/list/"+nPage;
        model.addAttribute("nPage", nPage);
        model.addAttribute("currentPage", currentPage);
        List<Address> addressList =addressService.addressListByNumberPageList(currentPage);
        model.addAttribute("addresses", addressList);
        return "address/management";
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

    @PostMapping("/list/{currentPage}/{id}/remove")
    public String removeById(@PathVariable Long currentPage, @PathVariable Long id){
        addressService.deleteById(id);
        return "redirect:/address/list/"+currentPage;
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
}
