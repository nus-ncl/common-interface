package sg.ncl.service.registration.data.jpa.entities;

import org.springframework.data.jpa.repository.JpaRepository;
import sg.ncl.service.registration.dtos.entities.RegistrationEntity;

/**
 * @author Yeo Te Ye
 */
public interface RegistrationRepository extends JpaRepository<RegistrationEntity, String> {

}
