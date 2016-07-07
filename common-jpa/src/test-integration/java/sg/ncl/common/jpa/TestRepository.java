package sg.ncl.common.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Christopher Zhong
 * @version 1.0
 */
public interface TestRepository extends JpaRepository<TestEntity, Long> {
}
