package sg.ncl.service.authentication.data.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by dcszwang on 11/3/2016.
 */
public interface PasswordResetRequestRepository extends JpaRepository<PasswordResetRequestEntity, Long> {
    PasswordResetRequestEntity findByHash(String hash);
    List<PasswordResetRequestEntity> findByUsername(String username);
}
