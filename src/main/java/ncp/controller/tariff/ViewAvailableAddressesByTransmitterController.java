package ncp.controller.tariff;

import ncp.controller.paging.AbstractSecondaryPagingController;
import ncp.model.Address;
import ncp.service.interfaces.TransmitterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/tariff/transmitter")
public class ViewAvailableAddressesByTransmitterController extends AbstractSecondaryPagingController {

    private final TransmitterService transmitterService;

    @Autowired
    public ViewAvailableAddressesByTransmitterController(TransmitterService transmitterService) {
        this.transmitterService = transmitterService;
    }

    @GetMapping("/availableAddresses/{idTransmitter}")
    public String setupById(@PathVariable Long idTransmitter) {
        return firstSecondaryPage(idTransmitter);
    }

    @GetMapping("/availableAddresses/{idTransmitter}/list/{currentPage}")
    public String setupByIdAndPage(@PathVariable Long idTransmitter, @PathVariable Long currentPage, ModelMap model) {
        if (currentPage < 1L)
            return firstSecondaryPage(idTransmitter);
        Long nPage = transmitterService.availableAddressPageCount(idTransmitter);
        if (currentPage > nPage)
            return lastSecondaryPage(idTransmitter);
        model.addAttribute("nPage", nPage);
        model.addAttribute("idTransmitter", idTransmitter);
        List<Address> addressList = transmitterService.availableAddressListByNumberPageListAndTransmitterId(
                currentPage, idTransmitter);
        model.addAttribute("addresses", addressList);
        model.addAttribute("currentPage", currentPage);
        return "tariff/view_transmitter_available_addresses";
    }

    @Override
    protected Long primaryPageCount() {
        return 1L;
    }

    @Override
    protected String getPrimaryPrefix() {
        return "/tariff/transmitter";
    }

    @Override
    protected Long secondaryPageCount(Long id) {
        return transmitterService.availableAddressPageCount(id);
    }

    @Override
    protected String getSecondaryPrefix() {
        return "/availableAddresses";
    }
}
