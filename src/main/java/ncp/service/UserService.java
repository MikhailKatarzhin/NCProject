package ncp.service;

import ncp.model.Personality;
import ncp.model.Role;
import ncp.model.Tariff;
import ncp.model.User;
import ncp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Set;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private HttpServletRequest httpServletRequest;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null)
            throw new UsernameNotFoundException("User not found");
        return user;
    }

    public User getRemoteUser(){
        return userRepository.findByUsername(httpServletRequest.getRemoteUser());
    }

    public Long getRemoteUserId(){
        return getRemoteUser().getId();
    }

    public String getRemoteUserEmail(){
        return getRemoteUser().getEmail();
    }

    public Personality getRemoteUserPersonality(){
        return getRemoteUser().getPersonality();
    }

    public User getUserById(long id){
        return userRepository.findById(id).orElse(null);
    }

    public User getUserByUsername(String username){
        return userRepository.findByUsername(username);
    }

    public User signUp(User user, Set<Role> roleSet){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoleSet(roleSet);
        user.setPersonality(new Personality());
        return userRepository.save(user);
    }

    public boolean emailExists(String email){
        return userRepository.countByEmail(email) != 0;
    }

    public User saveEmail(String email){
        User user = getRemoteUser();
        user.setEmail(email);
        return userRepository.save(user);
    }

}
