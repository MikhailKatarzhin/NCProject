package ncp.service.implementations;

import ncp.model.Personality;
import ncp.repository.PersonalityRepository;
import ncp.service.interfaces.PersonalityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PersonalityServiceImp implements PersonalityService {

    PersonalityRepository personalityRepository;

    @Autowired
    public PersonalityServiceImp(PersonalityRepository personalityRepository) {
        this.personalityRepository = personalityRepository;
    }

    public Personality save(Personality personality) {
        return personalityRepository.save(personality);
    }
}
