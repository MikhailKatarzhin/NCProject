package ncp.controller;

import ncp.controller.paging.AbstractSecondaryPagingController;
import ncp.model.*;
import ncp.service.interfaces.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/contract")
public class ContractController extends AbstractSecondaryPagingController {

    private final ContractService contractService;
    private final AddressService addressService;
    private final WalletService walletService;
    private final TariffService tariffService;
    private final UserService userService;

    @Autowired
    public ContractController(ContractService contractService, TariffService tariffService
            , WalletService walletService, UserService userService, AddressService addressService) {
        this.contractService = contractService;
        this.addressService = addressService;
        this.walletService = walletService;
        this.tariffService = tariffService;
        this.userService = userService;
    }

///********************! Contract management !********************

    @GetMapping
    public String management() {
        return firstPage();
    }

    @GetMapping("/list/{currentPage}")
    public String managementByOffset(@PathVariable Long currentPage, ModelMap model) {
        if (currentPage < 1L)
            return firstPage();
        Long nPage = contractService.pageCount(userService.getRemoteUserId());
        if (currentPage > nPage)
            return lastPage();
        model.addAttribute("nPage", nPage);
        model.addAttribute("currentPage", currentPage);
        List<Contract> contractList = contractService.contractListByConsumerIdAndNumberPageList(
                userService.getRemoteUserId(), currentPage);
        model.addAttribute("contracts", contractList);
        return "contract/management";
    }

    @PostMapping("/terminate/{currentPage}/{id}")
    @PreAuthorize("@userServiceImp.getRemoteUser().getContract().contains(contractServiceImp.getById(#id))")
    public String terminateById(@PathVariable Long currentPage, @PathVariable Long id) {
        contractService.terminateContractById(id);
        return toPage(currentPage);
    }

    @PostMapping("/signContract/{tariffId}/to_address/{addressId}")
    public String signContract(@PathVariable Long tariffId, @PathVariable Long addressId, ModelMap model) {
        User user = userService.getRemoteUser();
        Tariff tariff = tariffService.getById(tariffId);
        if (walletService.debitingFunds(tariff.getPrice(), user.getId())){
            contractService.signContract(tariff, user, addressService.getById(addressId));
            walletService.replenishmentFunds(tariff.getPrice(), tariff.getProvider().getId());
            return firstPage();
        }
        model.addAttribute("paymentError"
                , "Your founds not enough for pay to sign contract with selected tariff");
        return selectTariff(addressId, model);
    }

///********************! Pagination Contract !********************

    @Override
    protected Long primaryPageCount() {
        return contractService.pageCount(userService.getRemoteUserId());
    }

    @Override
    protected String getPrimaryPrefix() {
        return "/contract";
    }

///********************! Tariff searching !********************

    @GetMapping("/select_address")
    public String selectAddress(ModelMap model) {
        return selectAddress(new Address(), model);
    }

    @GetMapping("/select_address/list")
    public String selectAddress(Address address, ModelMap model) {
        if (address == null)
            address = new Address();
        model.addAttribute("searchAddress", address);
        model.addAttribute("addresses", addressService.searchAddressLikeAddress(address, 1L));
        int i = 0;
        return "contract/select_address";
    }

    @GetMapping("/select_tariff/{addressId}")
    public String selectTariff(@PathVariable Long addressId, ModelMap model) {
        return selectTariff(addressId, 1L, new Tariff(), model);
    }

    @GetMapping("/select_tariff/{addressId}/list/{currentPage}")
    public String selectTariff(@PathVariable Long addressId, @PathVariable Long currentPage
            , Tariff tariff, ModelMap model) {
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("address", addressService.getById(addressId));
        model.addAttribute("searchTariff", tariff);
        model.addAttribute("nPage"
                , tariffService.countPageByAvailableAddressIdAndTurnedOnTransmitterAndActiveTariff(addressId));
        model.addAttribute("tariffs"
                , tariffService.selectByLimitOffsetAndAvailableAddressIdAndTurnedOnTransmitterAndActiveTariff(
                        currentPage, addressId));
        return "contract/select_tariff";
    }

///********************! Pagination Tariff !********************

    @Override
    protected Long secondaryPageCount(Long id) {
        return tariffService.countByAvailableAddressIdAndTurnedOnTransmitterAndActiveTariff(id);
    }

    @Override
    protected String getSecondaryPrefix() {
        return "/select_tariff";
    }
}
