package ncp.controller;

import ncp.model.User;
import ncp.service.interfaces.RoleService;
import ncp.service.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;


@RequestMapping("/sign_up")
@Controller
public class SignUpController {

    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public SignUpController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping
    public String signUp(ModelMap model) {
        if (userService.getRemoteUser() != null)
            return "redirect:/profile";
        model.addAttribute("user", new User());
        model.addAttribute("roles"
                , roleService.findAllExceptName("ADMINISTRATOR"));
        return "sign_up";
    }

    @Transactional
    @PostMapping
    public String addUser(Long[] rolesId, @RequestParam String confirmPassword, User user, ModelMap model) {
        if (userService.getByUsername(user.getUsername()) != null) {
            model.addAttribute("usernameExistsError", "Username already exists");
        }
        if (user.getPassword().isBlank()) {
            model.addAttribute("passwordIsBlankError", "Password must be not blank!");
        }
        if (!user.getPassword().equals(confirmPassword)) {
            model.addAttribute("passwordsAreDifferent", "Passwords are different");
        }
        if (userService.emailExists(user.getEmail())) {
            model.addAttribute("emailExistsError", "Email already exists");
        }
        if (rolesId == null) {
            model.addAttribute("roleRequired", "Required at least 1 role");
        } else if (rolesId.length == 0){
            model.addAttribute("roleRequired", "Required at least 1 role");
        }
        if (model.size() > 2) {
            model.addAttribute("roles"
                    , roleService.findAllExceptName("ADMINISTRATOR"));
            return "sign_up";
        }
        userService.signUp(user, roleService.getRoleSetByIds(rolesId));
        return "redirect:/sign_in";
    }
}
