package ncp.controller.tariff;

import ncp.controller.paging.AbstractTwosomeSecondaryPagingController;
import ncp.model.*;
import ncp.service.interfaces.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/tariff")
public class TariffController extends AbstractTwosomeSecondaryPagingController {

    private final TariffStatusService tariffStatusService;
    private final TransmitterService transmitterService;
    private final WalletService walletService;
    private final TariffService tariffService;
    private final UserService userService;

    @Autowired
    public TariffController(TariffStatusService tariffStatusService, TransmitterService transmitterService
            , WalletService walletService, TariffService tariffService, UserService userService) {
        this.tariffStatusService = tariffStatusService;
        this.transmitterService = transmitterService;
        this.walletService = walletService;
        this.tariffService = tariffService;
        this.userService = userService;
    }

///********************! Transmitter management !********************

    @GetMapping
    public String management() {
        return firstPage();
    }

    @PostMapping("/add")
    public String tariffAdd(Tariff tariff, ModelMap model) {
        model = tariffService.validateNewTariff(tariff, model);
        if (!model.containsAttribute("hasError"))
            tariffService.saveNew(tariff);
        return firstPage();
    }

    @PostMapping("/list/{numberPageList}/{id}/remove")
    @PreAuthorize("@userServiceImp.getRemoteUser().getTariff().contains(@tariffServiceImp.getById(#id))")
    public String removeById(@PathVariable Long numberPageList, @PathVariable Long id) {
        tariffService.deleteById(id);
        return toPage(numberPageList);
    }

    @GetMapping("/list/{numberPageList}")
    public String managementByOffset(@PathVariable Long numberPageList, ModelMap model) {
        if (numberPageList < 1L)
            return firstPage();
        Long nPage = tariffService.pageCount();
        if (numberPageList > nPage)
            return lastPage();
        model.addAttribute("nPage", nPage);
        model.addAttribute("currentPage", numberPageList);
        model.addAttribute("tariffForm", new Tariff());
        List<Tariff> tariffList = tariffService.ownTariffListByNumberPageList(numberPageList, userService.getRemoteUserId());
        model.addAttribute("tariffs", tariffList);
        return "tariff/management";
    }

///********************! Pagination Tariff !********************

    @Override
    protected Long primarySinglePageCount() {
        return tariffService.pageCount();
    }

    @Override
    protected String getPrimarySinglePrefix() {
        return "/tariff";
    }

///********************! Setup of Tariff !********************

    @GetMapping("/setup/{id}")
    public String setupById(@PathVariable Long id) {
        return firstSecondaryPage(id);
    }

    @GetMapping("/setup/{id}/list/{connectedTransmitterPage}")
    @PreAuthorize("@userServiceImp.getRemoteUser().getTariff().contains(@tariffServiceImp.getById(#id))")
    public String setupByIdAndPage(@PathVariable Long id, @PathVariable Long connectedTransmitterPage, ModelMap model) {
        if (connectedTransmitterPage < 1L)
            return firstSecondaryPage(id);
        Long nPage = tariffService.connectedTransmitterPageCount(id);
        if (connectedTransmitterPage > nPage)
            return lastSecondaryPage(id);
        model.addAttribute("nPage", nPage);
        List<Transmitter> transmitterList = tariffService.connectedTransmitterListByNumberPageListAndTariffId(
                connectedTransmitterPage, id);
        model.addAttribute("transmitters", transmitterList);
        model.addAttribute("connectedTransmitterPage", connectedTransmitterPage);
        model.addAttribute("tariff", tariffService.getById(id));
        model.addAttribute("statusList", tariffStatusService.getAll());
        return "tariff/setup";
    }

    @PostMapping("/setup/{id}/{connectedTransmitterPage}/status")
    @PreAuthorize("@userServiceImp.getRemoteUser().getTariff().contains(@tariffServiceImp.getById(#id))")
    public String setStatusByTariffId(
            @PathVariable Long id
            , @PathVariable Long connectedTransmitterPage
            , @RequestParam Long statusSelectId, ModelMap model) {
        if (tariffService.getById(id).getStatus().getName().equals("inactive")){
            Wallet wallet = tariffService.getById(id).getProvider().getWallet();
            Long payment = tariffService.countConnectedAddressByTariffId(id);
            if (!wallet.debitingFunds(payment)){
                model.addAttribute("paymentError"
                        , "Your funds are not enough to activate this tariff");
                return setupByIdAndPage(id, connectedTransmitterPage, model);
            }
        }
        tariffService.setStatus(id, statusSelectId);
        return toSecondaryPage(connectedTransmitterPage, id);
    }

    @PostMapping("/setup/{id}/{connectedTransmitterPage}/description")
    @PreAuthorize("@userServiceImp.getRemoteUser().getTariff().contains(@tariffServiceImp.getById(#id))")
    public String setDescriptionByTariffId(
            @PathVariable Long id
            , @PathVariable Long connectedTransmitterPage
            , @RequestParam String description) {
        tariffService.setDescription(id, description);
        return toSecondaryPage(connectedTransmitterPage, id);
    }

    @PostMapping("/setup/{id}/{connectedTransmitterPage}/price")
    @PreAuthorize("@userServiceImp.getRemoteUser().getTariff().contains(@tariffServiceImp.getById(#id))")
    public String setPriceByTariffId(
            @PathVariable Long id
            , @PathVariable Long connectedTransmitterPage
            , @RequestParam Long price) {
        tariffService.setPrice(id, price);
        return toSecondaryPage(connectedTransmitterPage, id);
    }

    @PostMapping("/setup/{id}/{connectedTransmitterPage}/disconnectTransmitter/{connectedTransmitterId}")
    @PreAuthorize("@userServiceImp.getRemoteUser().getTariff().contains(@tariffServiceImp.getById(#id))")
    public String disconnectTransmitterById(
            @PathVariable Long id
            , @PathVariable Long connectedTransmitterPage
            , @PathVariable Long connectedTransmitterId) {
        tariffService.removeConnectedTransmitterByTransmitterIdAndTariffId(connectedTransmitterId, id);
        return toSecondaryPage(connectedTransmitterPage, id);
    }

    @PostMapping("/setup/{id}/connectableTransmitters/{transmitterId}")
    @PreAuthorize("@userServiceImp.getRemoteUser().getTariff().contains(@tariffServiceImp.getById(#id))")
    public String connectTransmitter(@PathVariable Long id, @PathVariable Long transmitterId
            , Address address, ModelMap model) {
        if (walletService.debitingFunds(transmitterService.countAvailableAddressByTransmitterId(transmitterId)
                , userService.getRemoteUserId())) {
            tariffService.addConnectedTransmitter(id, transmitterId);
            return searchConnectableTransmitters(id, address, model);
        }
        model.addAttribute("paymentError"
                , "Your funds are not enough to pay for this transmitter [" + transmitterId + "]");
        return searchConnectableTransmitters(id, address, model);
    }

///********************! Search connectable Transmitters !********************

    @GetMapping("/setup/{id}/connectableTransmitters")
    public String searchConnectableTransmitters(@PathVariable Long id, ModelMap model) {
        return searchConnectableTransmitters(id, new Address(), model);
    }

    @GetMapping("/setup/{id}/connectableTransmitters/list")
    @PreAuthorize("@userServiceImp.getRemoteUser().getTariff().contains(@tariffServiceImp.getById(#id))")
    public String searchConnectableTransmitters(@PathVariable Long id, Address address, ModelMap model) {
        if (address == null)
            address = new Address();
        model.addAttribute("tariffId", id);
        model.addAttribute("searchAddress", address);
        model.addAttribute("transmitters"
                , tariffService.searchTransmitterByTransmitterAvailableAddressIdWithoutConnectedTransmitterId(
                        address, 1L, id));
        return "tariff/add_connectable_transmitter";
    }

///********************! Pagination Transmitters !********************

    @Override
    protected Long firstSecondaryPageCount(Long id) {
        return tariffService.connectedTransmitterPageCount(id);
    }

    @Override
    protected String getFirstSecondaryPrefix() {
        return "/setup";
    }

///********************! Contracts view !********************

    @GetMapping("/contract/{id}")
    public String contractsViewByTariffId(@PathVariable Long id) {
        return firstSecondSecondaryPage(id);
    }

    @GetMapping("/contract/{id}/list/{signedContractPage}")
    @PreAuthorize("@userServiceImp.getRemoteUser().getTariff().contains(@tariffServiceImp.getById(#id))")
    public String contractsViewByTariffId(@PathVariable Long id, @PathVariable Long signedContractPage, ModelMap model) {
        if (signedContractPage < 1L)
            return firstSecondSecondaryPage(id);
        Long nPage = tariffService.countPageContractByTariffId(id);
        if (signedContractPage > nPage)
            return lastSecondSecondaryPage(id);
        model.addAttribute("nPage", nPage);
        model.addAttribute("primaryId", id);
        List<Contract> contractList = tariffService.signedContractListsByTariffId(id, signedContractPage);
        model.addAttribute("contracts", contractList);
        model.addAttribute("signedContractPage", signedContractPage);
        return "tariff/view_contracts";
    }

    @PostMapping("/contract/{id}/list/{signedContractPage}/terminate_contract/{contractId}")
    @PreAuthorize("@userServiceImp.getRemoteUser().getTariff().contains(@tariffServiceImp.getById(#id))")
    public String terminateContractById(@PathVariable Long id, @PathVariable Long signedContractPage
            , @PathVariable Long contractId) {
        tariffService.terminateContractById(contractId);
        return toSecondSecondaryPage(signedContractPage, id);
    }

///********************! Pagination contracts !********************

    @Override
    protected Long secondSecondaryPageCount(Long id) {
        return tariffService.countPageContractByTariffId(id);
    }

    @Override
    protected String getSecondSecondaryPrefix() {
        return "/contract";
    }
}
