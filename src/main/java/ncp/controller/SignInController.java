package ncp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;


@Controller
public class SignInController {

    private final HttpServletRequest httpServletRequest;

    public SignInController(HttpServletRequest httpServletRequest) {
        this.httpServletRequest = httpServletRequest;
    }


    @GetMapping("/sign_in")
    public String signIn(){
        if (httpServletRequest.getRemoteUser() != null)
            return "redirect:/profile";
        return "sign_in";
    }
}
