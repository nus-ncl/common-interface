package sg.ncl.service.registration.data.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Yeo Te Ye
 */
public interface RegistrationRepository extends JpaRepository<RegistrationEntity, String> {

}
