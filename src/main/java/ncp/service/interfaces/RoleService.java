package ncp.service.interfaces;

import ncp.model.Role;
import java.util.List;
import java.util.Set;

public interface RoleService {

    List<Role> findAllExceptName(String exceptingName);

    Set<Role> getRoleSetByIds(Long[] ids);
}
