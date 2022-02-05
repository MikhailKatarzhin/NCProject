package ncp.controller;

import ncp.model.Tariff;
import ncp.model.Transmitter;
import ncp.service.TariffService;
import ncp.service.TariffStatusService;
import ncp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/tariff")
public class TariffController extends AbstractTwosomeSecondaryPagingController{

    @Autowired
    private TariffService tariffService;
    @Autowired
    private TariffStatusService tariffStatusService;
    @Autowired
    private HttpServletRequest httpServletRequest;
    @Autowired
    private UserService userService;

///********************! Transmitter management !********************

    @GetMapping
    public String management(){
        return firstPage();
    }

    @PostMapping("/add")
    public String tariffAdd(Tariff tariff, ModelMap model){
        model = tariffService.validateNewTariff(tariff, model);
        if (!model.containsAttribute("hasError"))
            tariffService.saveNew(tariff);
        return firstPage();
    }

    @PostMapping("/list/{numberPageList}/{id}/remove")
    public String removeById(@PathVariable Long numberPageList, @PathVariable Long id){
        tariffService.deleteById(id);
        return toPage(numberPageList);
    }

    @GetMapping("/list/{numberPageList}")
    public String managementByOffset(@PathVariable Long numberPageList, ModelMap model){
        if (numberPageList < 1L)
            return firstPage();
        long nPage=tariffService.pageCount();
        if (numberPageList > nPage)
            return lastPage();
        model.addAttribute("nPage", nPage);
        model.addAttribute("currentPage", numberPageList);
        model.addAttribute("tariffForm", new Tariff());
        List<Tariff> tariffList =tariffService.ownTariffListByNumberPageList(numberPageList, userService.getRemoteUserId());
        model.addAttribute("tariffs", tariffList);
        return "tariff/management";
    }

///********************! Pagination Tariff !********************

    @Override
    protected Long primarySinglePageCount(){
        return tariffService.pageCount();
    }

    @Override
    protected String getPrimarySinglePrefix(){
        return "/tariff";
    }

///********************! Setup of Tariff !********************

    @GetMapping("/setup/{id}")
    public String setupById(@PathVariable Long id){
        return firstSecondaryPage(id);
    }

    @GetMapping("/setup/{id}/list/{connectedTransmitterPage}")
    public String setupByIdAndPage(@PathVariable Long id, @PathVariable Long connectedTransmitterPage, ModelMap model){
        if (connectedTransmitterPage < 1L)
            return firstSecondaryPage(id);
        long nPage=tariffService.connectedTransmitterPageCount(id);
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
    public String setStatusByTariffId(
            @PathVariable Long id
            , @PathVariable Long connectedTransmitterPage
            , @RequestParam Long statusSelectId){
        tariffService.setStatus(id, statusSelectId);
        return toSecondaryPage(connectedTransmitterPage, id);
    }

    @PostMapping("/setup/{id}/{connectedTransmitterPage}/description")
    public String setDescriptionByTariffId(
            @PathVariable Long id
            , @PathVariable Long connectedTransmitterPage
            , @RequestParam String description){
        tariffService.setDescription(id, description);
        return toSecondaryPage(connectedTransmitterPage, id);
    }

    @PostMapping("/setup/{id}/{connectedTransmitterPage}/price")
    public String setPriceByTariffId(
            @PathVariable Long id
            , @PathVariable Long connectedTransmitterPage
            , @RequestParam long price){
        tariffService.setPrice(id, price);
        return toSecondaryPage(connectedTransmitterPage, id);
    }

    @PostMapping("/setup/{id}/{connectedTransmitterPage}/disconnectTransmitter/{connectedTransmitterId}")
    public String disconnectTransmitterById(
            @PathVariable Long id
            , @PathVariable Long connectedTransmitterPage
            , @PathVariable Long connectedTransmitterId){
        tariffService.removeConnectedTransmitterByTransmitterIdAndTariffId(connectedTransmitterId, id);
        return toSecondaryPage(connectedTransmitterPage, id);
    }

///********************! Pagination Transmitters !********************

    @Override
    protected Long firstSecondaryPageCount(Long id){
        return tariffService.connectedTransmitterPageCount(id);
    }

    @Override
    protected String getFirstSecondaryPrefix(){
        return "/setup";
    }

///********************! Pagination contracts !********************

    @Override
    protected Long secondSecondaryPageCount(Long id) {
        return null;
    }

    @Override
    protected String getSecondSecondaryPrefix() {
        return "/contract";
    }
}
