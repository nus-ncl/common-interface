package sg.ncl.service.data.data.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import sg.ncl.service.data.domain.DataVisibility;

import java.util.List;

/**
 * Created by dcszwang on 10/6/2016.
 */
public interface DataRepository extends JpaRepository<DataEntity, Long> {

    List<DataEntity> findByVisibility(DataVisibility visibility);

    List<DataEntity> findByName(String name);

}
