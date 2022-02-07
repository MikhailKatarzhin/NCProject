package ncp.service.interfaces;

import ncp.model.Personality;
import ncp.model.Role;
import ncp.model.User;

import java.util.Set;


public interface UserService {

    User getRemoteUser();

    Long getRemoteUserId();

    String getRemoteUserEmail();

    Personality getRemoteUserPersonality();

    User getById(Long id);

    User getByUsername(String username);

    User signUp(User user, Set<Role> roleSet);

    boolean emailExists(String email);

    User saveEmail(String email);

}
