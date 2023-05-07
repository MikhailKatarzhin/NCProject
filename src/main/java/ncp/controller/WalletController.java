package ncp.controller;

import ncp.model.Contract;
import ncp.service.interfaces.payment.ContractPaymentService;
import ncp.service.interfaces.ContractService;
import ncp.service.interfaces.UserService;
import ncp.service.interfaces.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/wallet")
public class WalletController {

    private final ContractPaymentService contractPaymentService;
    private final ContractService contractService;
    private final WalletService walletService;
    private final UserService userService;

    @Autowired
    public WalletController(ContractPaymentService contractPaymentService, ContractService contractService, WalletService walletService, UserService userService) {
        this.contractPaymentService = contractPaymentService;
        this.contractService = contractService;
        this.walletService = walletService;
        this.userService = userService;
    }

    @GetMapping("/replenishment")
    public String replenishment(ModelMap model) {
        model.addAttribute("balance", walletService.getBalance(userService.getRemoteUserId()));
        return "wallet/replenishment";
    }

    @PostMapping("/replenishment")
    public String replenishment(@RequestParam Long incomeFounds, ModelMap model) {
        Long userId = userService.getRemoteUserId();
        if (walletService.replenishmentFunds(incomeFounds, userId)) {
            Long nRow = contractService.countExpiredContractWithNonInactiveTariffByLimitOffsetByConsumerId(userId);
            Long rowInPage = 100L;
            long nPage = nRow / rowInPage + (nRow % rowInPage == 0 ? 0 : 1);
            for (; nPage > 0; nPage--){
                List<Contract> contractList = contractService.listExpiredContractWithNonInactiveTariffByLimitOffsetByConsumerIdLimitOffset(
                        userId, rowInPage, (nPage - 1) * rowInPage);
                for (Contract contract: contractList)
                    if (!contractPaymentService.contractPayment(contract)){
                        nPage = 0L;
                        break;
                    }
            }
            return "redirect:/profile";
        }
        model.addAttribute("replenishmentFailed", "Replenishment failed");
        return replenishment(model);
    }
}
