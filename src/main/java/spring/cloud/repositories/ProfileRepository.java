package spring.cloud.repositories;

import org.springframework.data.repository.CrudRepository;
import spring.cloud.entities.Profile;

public interface ProfileRepository extends CrudRepository<Profile, Long> {
}