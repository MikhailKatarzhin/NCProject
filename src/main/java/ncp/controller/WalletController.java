package ncp.controller;

import ncp.service.interfaces.UserService;
import ncp.service.interfaces.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/wallet")
public class WalletController {

    private final WalletService walletService;
    private final UserService userService;

    @Autowired
    public WalletController(WalletService walletService, UserService userService){
        this.walletService = walletService;
        this.userService = userService;
    }

    @GetMapping("/replenishment")
    public String replenishment(ModelMap model){
        model.addAttribute("balance", walletService.getBalance(userService.getRemoteUserId()));
        return "wallet/replenishment";
    }

    @PostMapping("/replenishment")
    public String replenishment(@RequestParam Long incomeFounds, ModelMap model){
        if (walletService.replenishmentFunds(incomeFounds, userService.getRemoteUserId())) {
            return "redirect:/profile";
        }
        model.addAttribute("replenishmentFailed", "Replenishment failed");
        return replenishment(model);
    }
}
