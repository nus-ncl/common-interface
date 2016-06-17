package sg.ncl.service.registration.data.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import sg.ncl.service.registration.data.jpa.entities.RegistrationEntity;

/**
 * @author Yeo Te Ye
 */
public interface RegistrationRepository extends JpaRepository<RegistrationEntity, String> {

}
