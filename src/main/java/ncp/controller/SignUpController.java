package ncp.controller;

import ncp.model.User;
import ncp.service.RoleService;
import ncp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/sign_up")
@Controller
public class SignUpController {
    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;

    @GetMapping
    public String signUp(ModelMap model){
        if (userService.getRemoteUser()!=null)
            return "redirect:/profile";
        model.addAttribute("roles"
                , roleService.findAllExceptName("ADMINISTRATOR"));
        return "sign_up";
    }

    @PostMapping
    public String addUser(@RequestParam("roles")Long[] rolesId, User user, ModelMap model){
        if (userService.getUserByUsername(user.getUsername()) != null){
            model.addAttribute("usernameExistsError", "Username already exists");
            return "sign_up";
        }
        if (userService.emailExists(user.getEmail())){
            model.addAttribute("emailExistsError", "Email already exists");
            return "sign_up";
        }
        userService.signUp(user, roleService.getRoleSetByIds(rolesId));
        return "redirect:/sign_in";
    }
}
