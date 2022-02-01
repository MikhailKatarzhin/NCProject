package ncp.controller;

import lombok.AllArgsConstructor;
import ncp.model.User;
import ncp.repository.ContractRepository;
import ncp.repository.PersonalityRepository;
import ncp.repository.RoleRepository;
import ncp.repository.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RequestMapping("/profile")
@Controller
@AllArgsConstructor
public class ProfilesController {
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PersonalityRepository personalityRepository;
    private ContractRepository contractRepository;
    private HttpServletRequest httpServletRequest;
    @GetMapping
    public String myProfile(){
        if (httpServletRequest.getRemoteUser()==null)
            return "/sign_up";
        return "redirect:/profile/"+userRepository.findByUsername(httpServletRequest.getRemoteUser()).getId();
    }

    @GetMapping("/{id}")
    public String viewProfileById(@PathVariable Long id, ModelMap model){
        try {
            User user = userRepository.findById(id).orElseThrow();
            model.addAttribute("user", user);
        }catch (Exception e){
            return "sign_in";
        }
        return "/profile";
    }

    @GetMapping("/change_email")
    public String changeEmail(ModelMap model){
        User user = userRepository.findByUsername(httpServletRequest.getRemoteUser());
        model.addAttribute("currentEmail", user.getEmail());
        return "change_email";
    }

    @PostMapping("/change_email")
    public String changeEmail(@RequestParam("email") String email, ModelMap model){
        if (userRepository.countByEmail(email) != 0){
            model.addAttribute("emailError", "Email already exists");
            return "/change_email";
        }
        User user = userRepository.findByUsername(httpServletRequest.getRemoteUser());
        user.setEmail(email);
        userRepository.save(user);
        return "redirect:/profile";
    }
}
