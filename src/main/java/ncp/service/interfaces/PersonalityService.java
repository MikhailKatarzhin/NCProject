package ncp.service.interfaces;

import ncp.model.Personality;
import org.springframework.stereotype.Service;

@Service
public interface PersonalityService {
    Personality save(Personality personality);
}
