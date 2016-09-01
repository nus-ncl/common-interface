package sg.ncl.service.user.data.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Christopher Zhong
 */
public interface UserRepository extends JpaRepository<UserEntity, String> {

    UserEntity findByUserDetailsEmail(String email);
}
