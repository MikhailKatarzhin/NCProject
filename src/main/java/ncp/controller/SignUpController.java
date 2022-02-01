package ncp.controller;

import ncp.model.Personality;
import ncp.model.Role;
import ncp.model.User;
import ncp.repository.RoleRepository;
import ncp.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashSet;
import java.util.Set;

@RequestMapping("/sign_up")
@Controller
public class SignUpController {
    private UserRepository userRepository;
    private RoleRepository roleRepository;

    @GetMapping
    public String signUp(ModelMap model){
        model.addAttribute("roles", roleRepository.findAllExceptName("ADMINISTRATOR"));
        return "sign_up";
    }

    @PostMapping
    public String addUser(@RequestParam("roles")Long[] rolesId, User user, ModelMap model){
        if (userRepository.findByUsername(user.getUsername()) != null){
            model.addAttribute("errorMessage", "User already exists");
            return "sign_up";
        }
        Set<Role> roleSet = new LinkedHashSet<>();
        for (Long roleId: rolesId) {
            roleSet.add(roleRepository.findById(roleId).orElseThrow());
        }
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        user.setRoleSet(roleSet);
        user.setPersonality(new Personality(user.getId(), "", "", ""));
        user.setTariffs(new LinkedHashSet<>());
        user.setContracts(new LinkedHashSet<>());
        userRepository.save(user);

        return "redirect:/sign_in";
    }
}
