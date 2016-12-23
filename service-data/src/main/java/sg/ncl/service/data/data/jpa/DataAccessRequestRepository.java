package sg.ncl.service.data.data.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by dcsjnh on 12/22/2016.
 */
public interface DataAccessRequestRepository extends JpaRepository<DataAccessRequestEntity, Long> {

    List<DataAccessRequestEntity> findByDataIdAndRequesterId(Long id, String requesterId);

}
