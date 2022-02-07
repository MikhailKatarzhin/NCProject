package ncp.service.implementations;

import ncp.model.Personality;
import ncp.repository.PersonalityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PersonalityServiceIMP {

    PersonalityRepository personalityRepository;

    @Autowired
    public PersonalityServiceIMP(PersonalityRepository personalityRepository){
        this.personalityRepository = personalityRepository;
    }

    public Personality save(Personality personality){
        return personalityRepository.save(personality);
    }
}
