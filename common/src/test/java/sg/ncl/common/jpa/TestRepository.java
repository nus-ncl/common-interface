package sg.ncl.common.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Chris on 8/7/2016.
 */
interface TestRepository extends JpaRepository<TestEntity, Long> {
}
