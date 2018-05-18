package sg.ncl.service.authentication.data.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by dcszwang on 11/3/2016.
 */
public interface PasswordResetRequestRepository extends JpaRepository<PasswordResetRequestEntity, Long> {
    PasswordResetRequestEntity findByHash(String hash);
    PasswordResetRequestEntity findByUsername(String username);
}
