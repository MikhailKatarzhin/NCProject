package ncp.controller;

import lombok.AllArgsConstructor;
import ncp.model.Personality;
import ncp.model.User;
import ncp.service.PersonalityService;
import ncp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/profile")
@Controller
@AllArgsConstructor
public class ProfilesController {
    @Autowired
    private PersonalityService personalityService;
    @Autowired
    private UserService userService;

    @GetMapping
    public String myProfile(){
        return "redirect:/profile/"+userService.getRemoteUserId();
    }

    @GetMapping("/{id}")
    public String viewProfileById(@PathVariable Long id, ModelMap model){
        User user = userService.getUserById(id);
        if (user == null)
            return myProfile();
        model.addAttribute("user", user);
        return "profile/profile";
    }

    @GetMapping("/change_email")
    public String changeEmail(ModelMap model){
        model.addAttribute("currentEmail", userService.getRemoteUserEmail());
        return "profile/change_email";
    }

    @PostMapping("/change_email")
    public String changeEmail(@RequestParam("email") String email, ModelMap model){
        if (userService.emailExists(email)){
            model.addAttribute("emailExistsError", "Email already exists");
            return "profile/change_email";
        }
        userService.saveEmail(email);
        return "redirect:/profile";
    }

    @GetMapping("/change_personality")
    public String changePersonality(ModelMap model){
        model.addAttribute("personality", userService.getRemoteUserPersonality());
        return "profile/change_personality";
    }

    @PostMapping("/change_personality")
    public String changePersonality(@ModelAttribute Personality personality){
        personalityService.save(personality);
        return "redirect:/profile";
    }
}
