package ncp.service.implementations;

import ncp.model.Role;
import ncp.repository.RoleRepository;
import ncp.service.interfaces.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Service
public class RoleServiceImp implements RoleService {

    private final RoleRepository roleRepository;

    @Autowired
    public RoleServiceImp(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public List<Role> findAllExceptName(String exceptingName) {
        return roleRepository.findAllExceptName(exceptingName);
    }

    public Set<Role> getRoleSetByIds(Long[] ids) {
        Set<Role> roleSet = new LinkedHashSet<>();
        for (Long roleId : ids) {
            roleRepository.findById(roleId).ifPresent(roleSet::add);
        }
        return roleSet;
    }
}
