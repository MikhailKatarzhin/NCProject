package ncp.controller;

import ncp.model.Personality;
import ncp.model.User;
import ncp.service.interfaces.PersonalityService;
import ncp.service.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/profile")
@Controller
public class ProfilesController {
    private final PersonalityService personalityService;
    private final UserService userService;

    @Autowired
    public ProfilesController(PersonalityService personalityService, UserService userService) {
        this.personalityService = personalityService;
        this.userService = userService;
    }

    @GetMapping
    public String myProfile() {
        return "redirect:/profile/" + userService.getRemoteUserId();
    }

    @GetMapping("/{id}")
    @PreAuthorize("(@userServiceImp.getRemoteUser().getId() == #id) or hasAuthority('ADMINISTRATOR')")
    public String viewProfileById(@PathVariable Long id, ModelMap model) {
        User user = userService.getById(id);
        if (user == null)
            return myProfile();
        model.addAttribute("user", user);
        return "profile/profile";
    }

    @GetMapping("/change_email")
    public String changeEmail(ModelMap model) {
        model.addAttribute("currentEmail", userService.getRemoteUserEmail());
        return "profile/change_email";
    }

    @PostMapping("/change_email")
    public String changeEmail(@RequestParam("email") String email, ModelMap model) {
        if (userService.emailExists(email)) {
            model.addAttribute("currentEmail", userService.getRemoteUserEmail());
            model.addAttribute("emailExistsError", "Email already exists");
            return "profile/change_email";
        }
        userService.saveEmail(email);
        return "redirect:/profile";
    }

    @GetMapping("/change_password")
    public String changePassword() {
        return "profile/change_password";
    }

    @PostMapping("/change_password")
    public String changePassword(
            @RequestParam("newPassword") String newPassword
            , @RequestParam("currentPassword") String currentPassword
            , @RequestParam("confirmPassword") String confirmPassword, ModelMap model
    ) {
        if (!userService.checkRemoteUserPassword(currentPassword)) {
            model.addAttribute("InvalidPasswordError", "Invalid password");
            return "profile/change_password";
        }
        if (newPassword.isBlank()) {
            model.addAttribute("passwordIsBlankError", "New password must be not null!");
            return "profile/change_password";
        }
        if (!confirmPassword.equals(newPassword)) {
            model.addAttribute("currentPassword", currentPassword);
            model.addAttribute("passwordIsDifferentError", "Passwords are different");
            return "profile/change_password";
        }
        userService.savePassword(newPassword);
        return "redirect:/profile";
    }

    @GetMapping("/change_personality")
    public String changePersonality(ModelMap model) {
        model.addAttribute("personality", userService.getRemoteUserPersonality());
        return "profile/change_personality";
    }

    @PostMapping("/change_personality")
    public String changePersonality(@ModelAttribute Personality personality) {
        personalityService.save(personality);
        return "redirect:/profile";
    }
}
