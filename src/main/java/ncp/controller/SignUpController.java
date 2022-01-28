package ncp.controller;

import ncp.model.Personality;
import ncp.model.Role;
import ncp.model.User;
import ncp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

@RequestMapping("/sign_up")
@Controller
public class SignUpController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public String signUp(){
        return "sign_up";
    }

    @PostMapping
    public String addUser(User user, Model model){
        if (userRepository.findByUsername(user.getUsername()) != null){
            model.addAttribute("errorMessage", "User already exists");
            return "sign_up";
        }

        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        user.setRoleSet(Collections.singleton(Role.CONSUMER));
        user.setPersonality(new Personality());
        user.setTariffs(new LinkedHashSet<>());
        user.setContracts(new LinkedHashSet<>());
        userRepository.save(user);

        return "redirect:/sign_in";
    }
}
