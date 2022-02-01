package ncp.repository;

import ncp.model.Personality;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonalityRepository extends CrudRepository<Personality, Long> {
}
