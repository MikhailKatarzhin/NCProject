package ncp.service;

import ncp.model.Personality;
import ncp.repository.PersonalityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PersonalityService {
    @Autowired
    PersonalityRepository personalityRepository;

    public Personality save(Personality personality){
        return personalityRepository.save(personality);
    }
}
