package ncp.service.implementations;

import ncp.model.Personality;
import ncp.repository.PersonalityRepository;
import ncp.service.interfaces.PersonalityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PersonalityServiceImp implements PersonalityService {

    private final PersonalityRepository personalityRepository;

    @Autowired
    public PersonalityServiceImp(PersonalityRepository personalityRepository) {
        this.personalityRepository = personalityRepository;
    }

    public Personality getById(long id) {
        return personalityRepository.findById(id).orElse(null);
    }

    public Personality save(Personality personality) {
        return personalityRepository.save(personality);
    }
}
