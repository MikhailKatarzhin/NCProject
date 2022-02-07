package ncp.controller;

import ncp.model.User;
import ncp.service.interfaces.RoleService;
import ncp.service.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;

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
        model.addAttribute("user", new User());
        model.addAttribute("roles"
                , roleService.findAllExceptName("ADMINISTRATOR"));
        model.addAttribute("selectedRoles", new ArrayList<Long>().add(0L));
        return "sign_up";
    }

    @PostMapping
    public String addUser(@RequestParam Long[] rolesId, @RequestParam String confirmPassword, User user, ModelMap model){
        if (userService.getByUsername(user.getUsername()) != null){
            model.addAttribute("usernameExistsError", "Username already exists");
        }
        if (!user.getPassword().equals(confirmPassword)){
            model.addAttribute("passwordsAreDifferent", "Passwords are different");
        }
        if (userService.emailExists(user.getEmail())){
            model.addAttribute("emailExistsError", "Email already exists");
        }
        if (rolesId.length==0){
            model.addAttribute("roleRequired", "Required at least 1 role");
        }
        if (model.size()>2){
            model.addAttribute("roles"
                    , roleService.findAllExceptName("ADMINISTRATOR"));
            model.addAttribute("selectedRoles", Arrays.asList(rolesId));
            return "sign_up";
        }
        userService.signUp(user, roleService.getRoleSetByIds(rolesId));
        return "redirect:/sign_in";
    }
}
