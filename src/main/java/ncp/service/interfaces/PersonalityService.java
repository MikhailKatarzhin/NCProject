package ncp.service.interfaces;

import ncp.model.Personality;

public interface PersonalityService {
    Personality getById(long id);
    Personality save(Personality personality);
}
