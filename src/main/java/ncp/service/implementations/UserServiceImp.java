package ncp.service.implementations;

import ncp.config.WebSecurityConfig;
import ncp.model.Personality;
import ncp.model.Role;
import ncp.model.User;
import ncp.model.Wallet;
import ncp.repository.UserRepository;
import ncp.repository.WalletRepository;
import ncp.service.interfaces.PersonalityService;
import ncp.service.interfaces.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImp implements UserService {

    private final static Logger logger = LoggerFactory.getLogger(UserServiceImp.class);

    private final UserRepository userRepository;
    private final PersonalityService personalityService;
    private final HttpServletRequest httpServletRequest;
    private final BCryptPasswordEncoder passwordEncoder;
    private final WalletRepository walletRepository;

    @Autowired
    public UserServiceImp(UserRepository userRepository, PersonalityService personalityService, HttpServletRequest httpServletRequest
            , BCryptPasswordEncoder passwordEncoder, WalletRepository walletRepository) {
        this.userRepository = userRepository;
        this.personalityService = personalityService;
        this.httpServletRequest = httpServletRequest;
        this.passwordEncoder = passwordEncoder;
        this.walletRepository = walletRepository;
    }

    public User getRemoteUser() {
        return userRepository.findUserByUsername(httpServletRequest.getRemoteUser());
    }

    public Long getRemoteUserId() {
        return getRemoteUser().getId();
    }

    public String getRemoteUserEmail() {
        return getRemoteUser().getEmail();
    }

    public Personality getRemoteUserPersonality() {
        return personalityService.getById(getRemoteUser().getId());
    }

    public boolean checkRemoteUserPassword(String password) {
        return WebSecurityConfig.encoder().matches(password, getRemoteUser().getPassword());
    }

    public User getById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public User getByUsername(String username) {
        return userRepository.findUserByUsername(username);
    }

    @Transactional
    public User signUp(User user, Set<Role> roleSet) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoleSet(roleSet);
        user = userRepository.save(user);
        Personality personality = new Personality();
        personality.setUser(user);
        personality.setFirstName("");
        personality.setLastName("");
        personality.setPatronymic("");
        personalityService.save(personality);
        Wallet wallet = new Wallet();
        wallet.setUser(user);
        wallet.setBalance(0L);
        walletRepository.save(wallet);
        logger.info("Signed up new user [id:{}] with roles: {}", user.getId()
                , user.getRoleSet().stream().map(Role::getName).collect(Collectors.joining(", ")));
        return user;
    }

    public boolean emailExists(String email) {
        return userRepository.countByEmail(email) != 0;
    }

    public User saveEmail(String email) {
        User user = getRemoteUser();
        String oldEmail = user.getEmail();
        user.setEmail(email);
        user =  userRepository.save(user);
        logger.info("User {}[id:{}] change email {} to {}", user.getUsername(), user.getId(), oldEmail, user.getEmail());
        return user;
    }

    @Override
    public User savePassword(String password) {
        User user = getRemoteUser();
        user.setPassword(WebSecurityConfig.encoder().encode(password));
        user = userRepository.save(user);
        logger.info("User {}[id:{}] change password to {}", user.getUsername(), user.getId(), password);
        return user;
    }
}
